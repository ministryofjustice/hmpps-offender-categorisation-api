package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange

import kotlinx.serialization.Serializable

/*
      "activeEscapeList": false,// REDACT as is from Nomis
      "activeEscapeRisk": false,// REDACT as is from Nomis
      "escapeListAlerts": [],// REDACT as is from Nomis
      "escapeRiskAlerts": [],// REDACT as is from Nomis
 */
@Serializable
data class Escape(
  val nomsId: String?,
  val riskType: String?,
  val provisionalCategorisation: String?,
)
