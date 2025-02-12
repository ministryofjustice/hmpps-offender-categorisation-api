package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.SdsExcludedOffenceCode

@Service
class ManageOffencesApiClient(
  @Qualifier("manageOffencesApiWebClient") private val webClient: WebClient,
) {
  fun checkWhichOffenceCodesAreSdsExcluded(offenceCodes: List<String>): List<SdsExcludedOffenceCode>? {
    return webClient.get()
      .uri { uriBuilder ->
        uriBuilder
          .path("/schedule/sds-early-release-exclusions")
          .queryParam("offenceCodes", offenceCodes)
          .build()
      }
      .retrieve()
      .bodyToMono(object : ParameterizedTypeReference<List<SdsExcludedOffenceCode>>() {})
      .block()
  }
}
