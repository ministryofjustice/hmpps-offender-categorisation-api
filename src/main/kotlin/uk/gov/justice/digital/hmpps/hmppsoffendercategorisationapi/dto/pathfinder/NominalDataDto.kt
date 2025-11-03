package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.pathfinder

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Nominal dto response from pathfinder API")
data class NominalDataDto(
  @Schema(description = "band", example = "3")
  val band: Int?,
)
