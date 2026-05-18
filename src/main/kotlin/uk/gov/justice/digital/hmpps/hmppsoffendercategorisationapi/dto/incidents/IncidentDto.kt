package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Incident dto from prison API")
data class IncidentDto(
  @Schema(description = "Type of the incident", example = "ASSAULT_5")
  val incidentType: String,
  @Schema(description = "Status of the incident", example = "CLOSED")
  val incidentStatus: String,
  @Schema(description = "List of questions and answers about the incident")
  val responses: List<IncidentResponseDto>,
)
