package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories

import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.Prison

class TestPrisonFactory {
  private var agencyId: String = "MDI"
  private var description: String = "Moorland HMP"
  private var active: Boolean = true

  fun withAgencyId(agencyId: String): TestPrisonFactory {
    this.agencyId = agencyId
    return this
  }
  fun withDescription(description: String): TestPrisonFactory {
    this.description = description
    return this
  }
  fun withActive(active: Boolean): TestPrisonFactory {
    this.active = active
    return this
  }

  fun build(): Prison {
    return Prison(
      agencyId = this.agencyId,
      description = this.description,
      active = this.active,
    )
  }
}
