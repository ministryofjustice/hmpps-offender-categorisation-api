package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.integration

import com.github.tomakehurst.wiremock.client.WireMock
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.config.JwtAuthHelper
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.config.PostgresContainer
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
abstract class IntegrationTestBase {

  private val frozenDateTime = "2025-01-01T10:40:34Z"
  val now = LocalDateTime.ofInstant(Instant.parse(frozenDateTime), ZoneId.of("UTC"))

  @Suppress("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  lateinit var webTestClient: WebTestClient

  @Autowired
  protected lateinit var jwtAuthHelper: JwtAuthHelper

  @MockitoSpyBean
  lateinit var clock: Clock

  @BeforeEach
  fun mockClock() {
    val fixedClock = Clock.fixed(Instant.parse(frozenDateTime), ZoneId.of("UTC"))
    whenever(clock.instant()).thenReturn(fixedClock.instant())
    whenever(clock.zone).thenReturn(fixedClock.zone)
  }

  companion object {
    private val pgContainer = PostgresContainer.instance
    internal val prisonerSearchMockServer = PrisonerSearchMockServer()
    internal val prisonApiMockServer = PrisonApiMockServer()
    internal val manageAdjudicationsMockServer = ManageAdjudicationsMockServer()
    internal val assessRisksAndNeedsMockServer = AssessRisksAndNeedsMockServer()
    internal val manageOffencesMockServer = ManageOffencesMockServer()
    internal val hmppsAuthMockServer = HmppsAuthMockServer()

    @JvmStatic
    @DynamicPropertySource
    fun properties(registry: DynamicPropertyRegistry) {
      pgContainer?.run {
        registry.add("spring.datasource.url", pgContainer::getJdbcUrl)
        registry.add("spring.datasource.username", pgContainer::getUsername)
        registry.add("spring.datasource.password", pgContainer::getPassword)
        registry.add("spring.datasource.placeholders.database_update_password", pgContainer::getPassword)
        registry.add("spring.datasource.placeholders.database_read_only_password", pgContainer::getPassword)
        registry.add("spring.flyway.url", pgContainer::getJdbcUrl)
        registry.add("spring.flyway.user", pgContainer::getUsername)
        registry.add("spring.flyway.password", pgContainer::getPassword)
      }
    }

    @BeforeAll
    @JvmStatic
    fun startMocks() {
      prisonerSearchMockServer.start()
      prisonApiMockServer.start()
      manageAdjudicationsMockServer.start()
      assessRisksAndNeedsMockServer.start()
      manageOffencesMockServer.start()
      hmppsAuthMockServer.start()
    }

    @AfterAll
    @JvmStatic
    fun stopMocks() {
      prisonerSearchMockServer.stop()
      prisonApiMockServer.stop()
      manageAdjudicationsMockServer.stop()
      assessRisksAndNeedsMockServer.stop()
      manageOffencesMockServer.stop()
      hmppsAuthMockServer.stop()
    }

    fun stubPing(status: Int) {
      hmppsAuthMockServer.stubFor(
        WireMock.get("/auth/health/ping").willReturn(
          WireMock.aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody(if (status == 200) "pong" else "some error")
            .withStatus(status),
        ),
      )

      prisonerSearchMockServer.stubFor(
        WireMock.get("/health/ping").willReturn(
          WireMock.aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody(if (status == 200) "pong" else "some error")
            .withStatus(status),
        ),
      )

      manageAdjudicationsMockServer.stubFor(
        WireMock.get("/health/ping").willReturn(
          WireMock.aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody(if (status == 200) "pong" else "some error")
            .withStatus(status),
        ),
      )

      prisonApiMockServer.stubFor(
        WireMock.get("/health/ping").willReturn(
          WireMock.aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody(if (status == 200) "pong" else "some error")
            .withStatus(status),
        ),
      )

      assessRisksAndNeedsMockServer.stubFor(
        WireMock.get("/health/ping").willReturn(
          WireMock.aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody(if (status == 200) "pong" else "some error")
            .withStatus(status),
        ),
      )

      manageOffencesMockServer.stubFor(
        WireMock.get("/health/ping").willReturn(
          WireMock.aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody(if (status == 200) "pong" else "some error")
            .withStatus(status),
        ),
      )
    }
  }

  @BeforeEach
  fun resetStubs() {
    hmppsAuthMockServer.resetAll()
    prisonerSearchMockServer.resetAll()
    prisonApiMockServer.resetAll()
    manageAdjudicationsMockServer.resetAll()
    assessRisksAndNeedsMockServer.resetAll()
    manageOffencesMockServer.resetAll()

    hmppsAuthMockServer.stubGrantToken()
  }

  protected fun setAuthorisation(
    user: String = "test-client",
    roles: List<String> = listOf(),
  ): (HttpHeaders) -> Unit = jwtAuthHelper.setAuthorisation(user, roles)

  fun setHeaders(contentType: MediaType = MediaType.APPLICATION_JSON, username: String? = "ITAG_USER", roles: List<String> = listOf()): (HttpHeaders) -> Unit = {
    it.setBearerAuth(jwtAuthHelper.createJwt(subject = username, roles = roles))
    it.contentType = contentType
  }
}
