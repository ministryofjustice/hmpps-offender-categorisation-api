package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response

import kotlinx.serialization.Serializable

@Serializable
data class SarResponse(
  val categorisationTool: CategorisationTool? = null,

  val riskProfiler: RiskProfiler? = null,
)
