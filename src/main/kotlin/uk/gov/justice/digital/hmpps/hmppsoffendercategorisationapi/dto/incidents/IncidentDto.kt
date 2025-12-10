package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Incident dto from prison API")
data class IncidentDto(
  @Schema(description = "Status of the incident", example = INCIDENT_STATUS_DUP)
  val incidentStatus: String,
  @Schema(description = "Date time when the incident was reported", example = "2018-02-11T08:00:00")
  val reportTime: String,
  @Schema(description = "List of questions and answers about the incident")
  val responses: List<IncidentResponseDto>,
) {
  companion object {
    const val INCIDENT_TYPE_ASSAULT = "ASSAULT"
    const val INCIDENT_TYPE_ASSAULTS3 = "ASSAULTS3"

    const val PARTICIPATION_ROLE_ACTINV = "ACTINV"
    const val PARTICIPATION_ROLE_ASSIAL = "ASSIAL"
    const val PARTICIPATION_ROLE_FIGHT = "FIGHT"
    const val PARTICIPATION_ROLE_IMPED = "IMPED"
    const val PARTICIPATION_ROLE_PERP = "PERP"
    const val PARTICIPATION_ROLE_SUSASS = "SUSASS"
    const val PARTICIPATION_ROLE_SUSINV = "SUSINV"

    const val INCIDENT_STATUS_DUP = "DUP"
  }
}
