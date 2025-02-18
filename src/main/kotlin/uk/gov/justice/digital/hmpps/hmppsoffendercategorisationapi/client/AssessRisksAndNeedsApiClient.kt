package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.RiskSummary

@Service
class AssessRisksAndNeedsApiClient(
  @Qualifier("assessRisksAndNeedsApiWebClient") private val webClient: WebClient,
) {
  fun findRiskSummary(crn: String): RiskSummary? {
    return webClient.get()
      .uri("/risks/crn/$crn/summary")
      .retrieve()
      .bodyToMono(object : ParameterizedTypeReference<RiskSummary>() {})
      .block()
  }
}
