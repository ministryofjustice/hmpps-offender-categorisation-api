package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response

import io.swagger.v3.oas.annotations.media.Schema
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.enum.RiskLevel

@Schema(description = "RiskSummary")
class RiskSummary(
  val overallRiskLevel: RiskLevel,
)
