package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.pathfinder.NominalDataDto

@Service
class PathfinderApiClient(
  @Qualifier("pathfinderWebClient") private val webClient: WebClient,
) {
  fun getNominalData(prisonerNumber: String): NominalDataDto? = webClient.get()
    .uri("pathfinder/nominal/noms-id/$prisonerNumber")
    .retrieve()
    .bodyToMono<NominalDataDto>()
    .block()
}
