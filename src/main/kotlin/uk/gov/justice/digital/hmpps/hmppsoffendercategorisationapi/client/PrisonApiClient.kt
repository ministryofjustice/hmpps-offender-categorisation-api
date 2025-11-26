package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentDto
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentDto.Companion.INCIDENT_TYPE_ASSAULT
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentDto.Companion.INCIDENT_TYPE_ASSAULTS3
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentDto.Companion.PARTICIPATION_ROLE_ACTINV
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentDto.Companion.PARTICIPATION_ROLE_ASSIAL
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentDto.Companion.PARTICIPATION_ROLE_FIGHT
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentDto.Companion.PARTICIPATION_ROLE_IMPED
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentDto.Companion.PARTICIPATION_ROLE_PERP
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentDto.Companion.PARTICIPATION_ROLE_SUSASS
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentDto.Companion.PARTICIPATION_ROLE_SUSINV
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.Prison

@Service
class PrisonApiClient(
  @Qualifier("prisonApiWebClient") private val webClient: WebClient,
) {
  fun findPrisons(): List<Prison> = webClient.get()
    .uri("/api/agencies/prisons")
    .retrieve()
    .bodyToMono(object : ParameterizedTypeReference<List<Prison>>() {})
    .block()!!

  fun getAssaultIncidents(prisonerNumber: String): List<IncidentDto> = webClient.get()
    .uri { uriBuilder ->
      uriBuilder
        .path("api/offenders/$prisonerNumber/incidents")
        .queryParam("incidentType", listOf(INCIDENT_TYPE_ASSAULT, INCIDENT_TYPE_ASSAULTS3))
        .queryParam(
          "participationRoles",
          listOf(
            PARTICIPATION_ROLE_ACTINV,
            PARTICIPATION_ROLE_ASSIAL,
            PARTICIPATION_ROLE_FIGHT,
            PARTICIPATION_ROLE_IMPED,
            PARTICIPATION_ROLE_PERP,
            PARTICIPATION_ROLE_SUSASS,
            PARTICIPATION_ROLE_SUSINV,
          ),
        )
        .build()
    }
    .retrieve()
    .bodyToMono(object : ParameterizedTypeReference<List<IncidentDto>>() {})
    .block()!!
}
