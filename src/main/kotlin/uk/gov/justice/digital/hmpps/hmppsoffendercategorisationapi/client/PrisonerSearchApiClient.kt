package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client

import Prisoner
import com.google.gson.Gson
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientException

@Service
class PrisonerSearchApiClient(
  @Qualifier("prisonerSearchApiWebClient") private val webClient: WebClient,
  private val gson: Gson,
) {
  fun findPrisonersByPrisonerNumbers(prisonerNumbers: List<String>): List<Prisoner> {
    val requestBody = HashMap<String, List<String>>()
    requestBody["prisonerNumbers"] = prisonerNumbers

    return try {
      webClient.post()
        .uri("/prisoner-search/prisoner-numbers")
        .contentType(APPLICATION_JSON)
        .body(BodyInserters.fromValue(gson.toJson(requestBody)))
        .retrieve()
        .bodyToMono(object : ParameterizedTypeReference<List<Prisoner>>() {})
        .block()!!
    } catch (wce: WebClientException) {
      log.error(wce.message)
      return emptyList()
    }
  }

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}
