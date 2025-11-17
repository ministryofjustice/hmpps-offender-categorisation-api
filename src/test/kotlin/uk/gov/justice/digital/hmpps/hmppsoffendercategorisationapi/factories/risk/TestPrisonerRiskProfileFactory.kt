package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.risk

import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.risk.EscapeAlert
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.risk.PrisonerRiskProfile

class TestPrisonerRiskProfileFactory {

  private var escapeRiskAlerts: List<EscapeAlert> = emptyList<EscapeAlert>()
  private var escapeListAlerts: List<EscapeAlert> = emptyList<EscapeAlert>()
  private var riskDueToSeriousOrganisedCrime: Boolean = false
  private var riskDueToViolence: Boolean = false

  fun build() = PrisonerRiskProfile(
    escapeRiskAlerts = escapeRiskAlerts,
    escapeListAlerts = escapeListAlerts,
    riskDueToSeriousOrganisedCrime = riskDueToSeriousOrganisedCrime,
    riskDueToViolence = riskDueToViolence,
  )
}
