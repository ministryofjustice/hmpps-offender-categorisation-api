package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.risk

import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.risk.EscapeAlert

class TestEscapeAlertFactory {

  private var dateCreated: String = "2020-01-01"
  private var expired: Boolean = false
  private var active: Boolean = true

  fun withDateCreated(dateCreated: String): TestEscapeAlertFactory {
    this.dateCreated = dateCreated
    return this
  }

  fun withExpired(expired: Boolean): TestEscapeAlertFactory {
    this.expired = expired
    return this
  }

  fun withActive(active: Boolean): TestEscapeAlertFactory {
    this.active = active
    return this
  }

  fun build() = EscapeAlert(
    dateCreated = this.dateCreated,
    expired = this.expired,
    active = this.active,
  )
}
