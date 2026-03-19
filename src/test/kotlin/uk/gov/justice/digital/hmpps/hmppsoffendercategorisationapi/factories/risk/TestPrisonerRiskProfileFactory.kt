package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.risk

import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.risk.EscapeAlert
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.risk.PrisonerRiskProfile

class TestPrisonerRiskProfileFactory {

  private var escapeRiskAlerts: List<EscapeAlert> = emptyList<EscapeAlert>()
  private var escapeListAlerts: List<EscapeAlert> = emptyList<EscapeAlert>()
  private var riskDueToSeriousOrganisedCrime: Boolean = false
  private var riskDueToViolence: Boolean = false

  fun withEscapeRiskAlerts(escapeRiskAlerts: List<EscapeAlert>): TestPrisonerRiskProfileFactory {
    this.escapeRiskAlerts = escapeRiskAlerts
    return this
  }

  fun withEscapeListAlerts(escapeListAlerts: List<EscapeAlert>): TestPrisonerRiskProfileFactory {
    this.escapeListAlerts = escapeListAlerts
    return this
  }

  fun withRiskDueToSeriousOrganisedCrime(riskDueToSeriousOrganisedCrime: Boolean): TestPrisonerRiskProfileFactory {
    this.riskDueToSeriousOrganisedCrime = riskDueToSeriousOrganisedCrime
    return this
  }

  fun withRiskDueToViolence(riskDueToViolence: Boolean): TestPrisonerRiskProfileFactory {
    this.riskDueToViolence = riskDueToViolence
    return this
  }

  fun build() = PrisonerRiskProfile(
    escapeRiskAlerts = escapeRiskAlerts,
    escapeListAlerts = escapeListAlerts,
    riskDueToSeriousOrganisedCrime = riskDueToSeriousOrganisedCrime,
    riskDueToViolence = riskDueToViolence,
  )
}
