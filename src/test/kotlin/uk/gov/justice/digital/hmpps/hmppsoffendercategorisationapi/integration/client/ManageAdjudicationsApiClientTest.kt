package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.integration.client

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.ManageAdjudicationsApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.TestAdjudicationFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.adjudication.Adjudication
import java.time.LocalDate
import java.time.LocalDateTime

class ManageAdjudicationsApiClientTest : IntegrationTestBase() {
  private lateinit var manageAdjudicationsApiClient: ManageAdjudicationsApiClient

  @BeforeEach
  fun beforeEach() {
    val webClient = WebClient.create("http://localhost:${manageAdjudicationsMockServer.port()}")
    manageAdjudicationsApiClient = ManageAdjudicationsApiClient(webClient)
  }

  @Nested
  inner class FindAdjudicationsByBookingId {
    @Test
    fun `handles correct response`() {
      manageAdjudicationsMockServer.stubFindAdjudicationsByBookingId(12, 5)
      val result = manageAdjudicationsApiClient.findAdjudicationsByBookingId(12)
      Assertions.assertThat(result?.bookingId).isEqualTo(12)
      Assertions.assertThat(result?.adjudicationCount).isEqualTo(5)
    }
  }

  @Nested
  inner class FindAdjudicationsByAgencyId {
    @Test
    fun `handles correct response`() {
      val testPrisonerNumber = "ABC123"
      val testCreatedDateTime = LocalDateTime.now().minusDays(5).toString()
      manageAdjudicationsMockServer.stubFindAdjudicationsByAgencyId(
        listOf(
          TestAdjudicationFactory()
            .withPrisonerNumber(testPrisonerNumber)
            .withStatus(Adjudication.STATUS_CHARGE_PROVEN)
            .withCreatedDateTime(testCreatedDateTime)
            .build(),
        ),
      )
      val result = manageAdjudicationsApiClient.findAdjudicationsByAgencyId(
        "HCI",
        LocalDate.now().minusMonths(3).toString(),
        listOf(Adjudication.STATUS_CHARGE_PROVEN),
        0,
      )
      Assertions.assertThat(result?.get(0)?.prisonerNumber).isEqualTo(testPrisonerNumber)
      Assertions.assertThat(result?.get(0)?.status).isEqualTo(Adjudication.STATUS_CHARGE_PROVEN)
      Assertions.assertThat(result?.get(0)?.createdDateTime).isEqualTo(testCreatedDateTime)
    }
  }
}
