package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SarResponse(
  val categorisationTool: CategorisationTool? = null,

  val riskProfiler: RiskProfiler? = null,
)
