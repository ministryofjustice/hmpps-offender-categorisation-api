package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.integration.client

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.ManageAdjudicationsApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.integration.IntegrationTestBase

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
}
