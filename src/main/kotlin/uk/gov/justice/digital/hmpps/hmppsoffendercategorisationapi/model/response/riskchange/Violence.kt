package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange

import kotlinx.serialization.Serializable

/*
      "activeEscapeList": false,// REDACT as is from Nomis
      "activeEscapeRisk": false,// REDACT as is from Nomis
      "escapeListAlerts": [],// REDACT as is from Nomis
      "escapeRiskAlerts": [],// REDACT as is from Nomis
 */
@Serializable
data class Violence(
  val nomsId: String? = null,
  val riskType: String? = null,
  val displayAssaults: Boolean? = null,
  val numberOfAssaults: Int? = null,
  val notifySafetyCustodyLead: Boolean? = null,
  val numberOfSeriousAssaults: Int? = null,
  val numberOfNonSeriousAssaults: Int? = null,
  val provisionalCategorisation: String? = null,
  val veryHighRiskViolentOffender: Boolean? = null,
)
