package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client

import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.Prisoner

@Service
class PrisonerSearchApiClient(
  @Qualifier("prisonerSearchApiWebClient") private val webClient: WebClient,
  private val gson: Gson,
) {
  fun findPrisonersByPrisonerNumbers(prisonerNumbers: List<String>): List<Prisoner> {
    val requestBody = HashMap<String, List<String>>()
    requestBody["prisonerNumbers"] = prisonerNumbers

    return webClient.post()
      .uri("/prisoner-search/prisoner-numbers")
      .contentType(APPLICATION_JSON)
      .body(BodyInserters.fromValue(gson.toJson(requestBody)))
      .retrieve()
      .bodyToMono(object : ParameterizedTypeReference<List<Prisoner>>() {})
      .block()!!
  }
}
