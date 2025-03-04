package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.probation.ProbationPerson

@Service
class ProbationSearchApiClient(
  @Qualifier("probationSearchApiWebClient") private val webClient: WebClient,
) {
  fun findProbationPersonsFromPrisonNumbers(prisonNumbers: List<String>): List<ProbationPerson> = webClient.post()
    .uri("/nomsNumbers")
    .contentType(MediaType.APPLICATION_JSON)
    .body(BodyInserters.fromValue(jacksonObjectMapper().writeValueAsString(prisonNumbers)))
    .retrieve()
    .bodyToMono(object : ParameterizedTypeReference<List<ProbationPerson>>() {})
    .block()!!
}
