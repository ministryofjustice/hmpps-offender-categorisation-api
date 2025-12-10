package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.prisonerAlert

import io.swagger.v3.oas.annotations.media.Schema
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.prisonerAlert.PrisonerAlertResponseDto.Companion.ALERT_CODE_ESCAPE_RISK

@Schema(description = "A summary of the alert")
data class PrisonerAlertCodeSummaryDto(
  @Schema(required = true, description = "Alert Code", example = ALERT_CODE_ESCAPE_RISK)
  val code: String,

  @Schema(required = true, description = "Alert Code Description", example = "Escape Risk")
  val description: String,
)
