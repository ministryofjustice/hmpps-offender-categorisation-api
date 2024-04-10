package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.ZonedDateTime

/*
      "activeEscapeList": false,// REDACT as is from Nomis
      "activeEscapeRisk": false,// REDACT as is from Nomis
      "escapeListAlerts": [],// REDACT as is from Nomis
      "escapeRiskAlerts": [],// REDACT as is from Nomis
 */
data class Escape(
  val nomsId: String?,
  val riskType: String?,
  val provisionalCategorisation: String?,
)
