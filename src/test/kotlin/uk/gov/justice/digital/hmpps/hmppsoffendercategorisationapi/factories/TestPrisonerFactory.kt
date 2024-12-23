package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories

import Prisoner

class TestPrisonerFactory {
  private var prisonerNumber = "123ABC"
  private var status = Prisoner.STATUS_ACTIVE_IN
  private var prisonId = "TEST"
  private var restrictedPatient = false

  fun withPrisonerNumber(prisonerNumber: String): TestPrisonerFactory {
    this.prisonerNumber = prisonerNumber
    return this
  }

  fun withStatus(status: String): TestPrisonerFactory {
    this.status = status
    return this
  }

  fun withPrisonId(prisonId: String): TestPrisonerFactory {
    this.prisonId = prisonId
    return this
  }

  fun withRestrictedPatient(restrictedPatient: Boolean): TestPrisonerFactory {
    this.restrictedPatient = restrictedPatient
    return this
  }

  fun build(): Prisoner {
    return Prisoner(
      prisonerNumber = this.prisonerNumber,
      status = this.status,
      prisonId = prisonId,
      restrictedPatient = this.restrictedPatient,
    )
  }
}
