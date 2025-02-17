package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange

/*
      "activeEscapeList": false,// REDACT as is from Nomis
      "activeEscapeRisk": false,// REDACT as is from Nomis
      "escapeListAlerts": [],// REDACT as is from Nomis
      "escapeRiskAlerts": [],// REDACT as is from Nomis
      "displayAssaults: boolean // REDACT
 */
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class Violence(
  val nomsId: String? = null,
  val numberOfAssaults: Int? = null,
  private val notifySafetyCustodyLead: Boolean? = null,
  val numberOfSeriousAssaults: Int? = null,
  val numberOfNonSeriousAssaults: Int? = null,
  val provisionalCategorisation: String? = null,
  private val veryHighRiskViolentOffender: Boolean? = null,
  private val riskType: String? = null,
) {
  val shouldNotifySafetyCustodyLead: String?
    get() = if (this.notifySafetyCustodyLead == null) {
      null
    } else if (this.notifySafetyCustodyLead) {
      "Yes"
    } else {
      "No"
    }

  val isVeryHighRiskViolentOffender: String?
    get() = if (this.veryHighRiskViolentOffender == null) {
      null
    } else if (this.veryHighRiskViolentOffender) {
      "Yes"
    } else {
      "No"
    }
}
