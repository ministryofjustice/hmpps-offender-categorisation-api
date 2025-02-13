package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.integration.client

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.TestPrisonFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.integration.IntegrationTestBase

class PrisonApiClientTest : IntegrationTestBase() {
  private lateinit var prisonApiClient: PrisonApiClient

  @BeforeEach
  fun beforeEach() {
    val webClient = WebClient.create("http://localhost:${prisonApiMockServer.port()}")
    prisonApiClient = PrisonApiClient(webClient)
  }

  @Nested
  inner class FindPrisons {
    @Test
    fun `handles correct response`() {
      val testAgencyId = "TEST"
      val testDescription = "Test Prison"
      prisonApiMockServer.stubFindPrisons(
        listOf(
          (TestPrisonFactory())
            .withAgencyId(testAgencyId)
            .withDescription(testDescription)
            .withActive(true)
            .build(),
        ),
      )
      val result = prisonApiClient.findPrisons()
      Assertions.assertThat(result.count()).isEqualTo(1)
      Assertions.assertThat(result[0].agencyId).isEqualTo(testAgencyId)
      Assertions.assertThat(result[0].description).isEqualTo(testDescription)
      Assertions.assertThat(result[0].active).isTrue()
    }
  }
}
