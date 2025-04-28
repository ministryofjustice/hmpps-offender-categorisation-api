package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.integration.client

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonerSearchApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.TestPrisonerFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.Prisoner
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.prisoner.ConvictedOffence
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.prisoner.CurrentIncentive
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.prisoner.Level
import java.time.Duration
import java.time.LocalDate

class PrisonerSearchApiClientTest : IntegrationTestBase() {
  private lateinit var prisonerSearchApiClient: PrisonerSearchApiClient

  @BeforeEach
  fun beforeEach() {
    val webClient = WebClient.create("http://localhost:${prisonerSearchMockServer.port()}")
    prisonerSearchApiClient = PrisonerSearchApiClient(webClient, Duration.parse("10s"))
  }

  @Nested
  inner class FindPrisonersByPrisonerNumber {
    @Test
    fun `handles correct response`() {
      prisonerSearchMockServer.stubFindPrisonerByPrisonerNumber(
        (TestPrisonerFactory()).withPrisonerNumber("123ABC")
          .withStatus(Prisoner.STATUS_ACTIVE_IN)
          .withReleaseDate(LocalDate.now())
          .withSentenceStartDate(LocalDate.now())
          .withCategory("D")
          .withLegalStatus("SENTENCED")
          .withConvictedOffencesResponse(listOf(ConvictedOffence("SX56027", "Indecent assault on woman over 16 years of age")))
          .withCurrentIncentive(CurrentIncentive(Level("STD", "Standard")))
          .build(),
      )
      val result = prisonerSearchApiClient.findPrisonersByPrisonerNumbers(listOf("123ABC"))
      Assertions.assertThat(result.size).isEqualTo(1)
      Assertions.assertThat(result[0].prisonerNumber).isEqualTo("123ABC")
      Assertions.assertThat(result[0].releaseDate).isEqualTo(LocalDate.now())
      Assertions.assertThat(result[0].category).isEqualTo("D")
      Assertions.assertThat(result[0].currentIncentive?.level?.code).isEqualTo("STD")
      Assertions.assertThat(result[0].legalStatus).isEqualTo("SENTENCED")
      Assertions.assertThat(result[0].sentenceStartDate).isEqualTo(LocalDate.now())
      Assertions.assertThat(result[0].allConvictedOffences?.get(0)?.offenceCode).isEqualTo("SX56027")
    }
  }
}
