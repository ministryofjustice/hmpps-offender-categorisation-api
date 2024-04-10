package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

/*
      "activeEscapeList": false,// REDACT as is from Nomis
      "activeEscapeRisk": false,// REDACT as is from Nomis
      "escapeListAlerts": [],// REDACT as is from Nomis
      "escapeRiskAlerts": [],// REDACT as is from Nomis
 */
@Serializable
data class Violence(
  val nomsId: String?,
  val riskType: String?,
  val displayAssaults: Boolean?,
  val numberOfAssaults: Int?,
  val notifySafetyCustodyLead: Boolean?,
  val numberOfSeriousAssaults: Int?,
  val numberOfNonSeriousAssaults: Int?,
  val provisionalCategorisation: String?,
  val veryHighRiskViolentOffender: Boolean?,
)
