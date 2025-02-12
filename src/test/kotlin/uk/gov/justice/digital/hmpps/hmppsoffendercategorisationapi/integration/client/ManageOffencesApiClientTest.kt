package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.integration.client

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.ManageOffencesApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.integration.IntegrationTestBase

class ManageOffencesApiClientTest : IntegrationTestBase() {
  private lateinit var manageOffencesApiClient: ManageOffencesApiClient

  @BeforeEach
  fun beforeEach() {
    val webClient = WebClient.create("http://localhost:${manageOffencesMockServer.port()}")
    manageOffencesApiClient = ManageOffencesApiClient(webClient)
  }

  @Nested
  inner class CheckWhichOffenceCodesAreSdsExcluded {
    @Test
    fun `handles correct response`() {
      val testOffenceCodes = listOf("CODE_ONE", "CODE_TWO")
      manageOffencesMockServer.stubCheckWhichOffenceCodesAreSdsExcluded(testOffenceCodes)
      val result = manageOffencesApiClient.checkWhichOffenceCodesAreSdsExcluded(testOffenceCodes)
      Assertions.assertThat(result?.count()).isEqualTo(1)
      Assertions.assertThat(result?.get(0)?.offenceCode).isEqualTo(testOffenceCodes[0])
      Assertions.assertThat(result?.get(0)?.schedulePart).isEqualTo("violence")
    }
  }
}
