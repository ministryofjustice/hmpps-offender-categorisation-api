package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.prisonRegister.PrisonRegisterPrison

@Service
class PrisonRegisterApiClient(
  @Qualifier("prisonRegisterApiWebClient") private val webClient: WebClient,
) {
  fun getPrisons(): List<PrisonRegisterPrison> = webClient.get()
    .uri("/prisons")
    .retrieve()
    .bodyToMono(object : ParameterizedTypeReference<List<PrisonRegisterPrison>>() {})
    .block()!!
}
