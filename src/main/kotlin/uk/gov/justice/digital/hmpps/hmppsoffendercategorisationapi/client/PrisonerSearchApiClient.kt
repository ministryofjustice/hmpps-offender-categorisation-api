package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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
) {
  fun findPrisonersByPrisonerNumbers(prisonerNumbers: List<String>): List<Prisoner> {
    val requestBody = HashMap<String, List<String>>()
    requestBody["prisonerNumbers"] = prisonerNumbers

    return webClient.post()
      .uri("/prisoner-search/prisoner-numbers")
      .contentType(APPLICATION_JSON)
      .body(BodyInserters.fromValue(jacksonObjectMapper().writeValueAsString(requestBody)))
      .retrieve()
      .bodyToMono(object : ParameterizedTypeReference<List<Prisoner>>() {})
      .block()!!
  }

  fun findPrisonersByAgencyId(agencyId: String, page: Int, size: Int): List<Prisoner> {
    return webClient.get()
      .uri("/prisoner-search/prison/$agencyId?page=$page&size=$size")
      .header("Content-Type", "application/json")
      .retrieve()
      .bodyToMono(object : ParameterizedTypeReference<SearchResult>() {})
      .block()!!
      .content
  }
}

data class SearchResult(
  val content: List<Prisoner>,
)
