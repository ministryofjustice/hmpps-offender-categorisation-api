package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.ClientUtility.Companion.isNotFoundError
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.exception.NotFoundException
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.Prisoner
import java.time.Duration

@Service
class PrisonerSearchApiClient(
  @Qualifier("prisonerSearchApiWebClient") private val webClient: WebClient,
  @Value("\${prisoner.search.timeout:10s}") private val apiTimeout: Duration,
) {
  fun findPrisoner(prisonerNumber: String): Prisoner = findPrisonerByIdAsMono(prisonerNumber)
    .onErrorResume { e ->
      if (!isNotFoundError(e)) {
        log.error("Failed to get prisoner with id - $prisonerNumber on prisoner search")
        Mono.error(e)
      } else {
        log.error("Prisoner with id - $prisonerNumber not found.")
        Mono.error { NotFoundException("Prisoner with id - $prisonerNumber not found on prisoner search") }
      }
    }
    .blockOptional(apiTimeout).orElseThrow { NotFoundException("Prisoner with id - $prisonerNumber not found on prisoner search") }

  private fun findPrisonerByIdAsMono(prisonerId: String): Mono<Prisoner> = webClient.get().uri("/prisoner/{prisonerId}", prisonerId)
    .retrieve()
    .bodyToMono()

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

  fun findPrisonersByAgencyId(agencyId: String, page: Int, size: Int): List<Prisoner> = webClient.get()
    .uri("/prisoner-search/prison/$agencyId?page=$page&size=$size")
    .header("Content-Type", "application/json")
    .retrieve()
    .bodyToMono(object : ParameterizedTypeReference<SearchResult>() {})
    .block()!!
    .content

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}

data class SearchResult(
  val content: List<Prisoner>,
)
