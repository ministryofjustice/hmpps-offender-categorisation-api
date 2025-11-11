package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.risk

data class PrisonerRiskProfile(
  val escapeRiskAlerts: List<EscapeAlert>,
  val escapeListAlerts: List<EscapeAlert>,
  val riskDueToSeriousOrganisedCrime: Boolean,
  val riskDueToViolence: Boolean,
)
