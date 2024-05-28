package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
/*
      "activeEscapeList": false,// REDACT as is from Nomis
      "activeEscapeRisk": false,// REDACT as is from Nomis
      "escapeListAlerts": [],// REDACT as is from Nomis
      "escapeRiskAlerts": [],// REDACT as is from Nomis
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class Escape(
  val nomsId: String?,
  val riskType: String?,
  val provisionalCategorisation: String?,
)
