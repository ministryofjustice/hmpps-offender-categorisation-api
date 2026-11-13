package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.dto.prisonRegister

import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.prisonRegister.PrisonRegisterPrison

class TestPrisonRegisterPrisonFactory {
  private var prisonId: String = "MDI"
  private var prisonName: String = "Moorland HMP"
  private var active: Boolean = true

  fun withPrisonId(prisonId: String): TestPrisonRegisterPrisonFactory {
    this.prisonId = prisonId
    return this
  }
  fun withPrisonName(prisonName: String): TestPrisonRegisterPrisonFactory {
    this.prisonName = prisonName
    return this
  }
  fun withActive(active: Boolean): TestPrisonRegisterPrisonFactory {
    this.active = active
    return this
  }

  fun build(): PrisonRegisterPrison = PrisonRegisterPrison(
    prisonId = this.prisonId,
    prisonName = this.prisonName,
    active = this.active,
  )
}
