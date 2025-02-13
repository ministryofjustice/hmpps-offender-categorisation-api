package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.Prison

@Service
class PrisonApiClient(
  @Qualifier("prisonApiWebClient") private val webClient: WebClient,
) {
  fun findPrisons(): List<Prison> {
    return webClient.get()
      .uri("/api/agencies/prisons")
      .retrieve()
      .bodyToMono(object : ParameterizedTypeReference<List<Prison>>() {})
      .block()!!
  }
}
