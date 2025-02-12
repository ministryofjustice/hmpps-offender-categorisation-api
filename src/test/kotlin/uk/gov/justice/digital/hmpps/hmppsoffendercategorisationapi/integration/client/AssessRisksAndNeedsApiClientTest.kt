package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.integration.client

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.AssessRisksAndNeedsApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.enum.RiskLevel

class AssessRisksAndNeedsApiClientTest : IntegrationTestBase() {
  private lateinit var assessRisksAndNeedsApiClient: AssessRisksAndNeedsApiClient

  @BeforeEach
  fun beforeEach() {
    val webClient = WebClient.create("http://localhost:${assessRisksAndNeedsMockServer.port()}")
    assessRisksAndNeedsApiClient = AssessRisksAndNeedsApiClient(webClient)
  }

  @Nested
  inner class FindAdjudicationsByBookingId {
    @Test
    fun `handles correct response`() {
      val testCrn = "ABC123"
      assessRisksAndNeedsMockServer.stubFindRiskSummary(testCrn, RiskLevel.HIGH)
      val result = assessRisksAndNeedsApiClient.findRiskSummary(testCrn)
      Assertions.assertThat(result?.overallRiskLevel).isEqualTo(RiskLevel.HIGH)
    }
  }
}
