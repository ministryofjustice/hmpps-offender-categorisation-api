package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.model.entity

import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.PrisonerRiskProfileEntity

class PrisonerRiskProfileEntityFactory {
  private var offenderNo: String = "A1234BC"
  private var riskProfile: String? = null
  private var calculatedAt: java.time.ZonedDateTime? = null

  fun withOffenderNo(offenderNo: String): PrisonerRiskProfileEntityFactory {
    this.offenderNo = offenderNo
    return this
  }

  fun withRiskProfile(riskProfile: String?): PrisonerRiskProfileEntityFactory {
    this.riskProfile = riskProfile
    return this
  }

  fun build() = PrisonerRiskProfileEntity(
    offenderNo = this.offenderNo,
    riskProfile = this.riskProfile,
    calculatedAt = this.calculatedAt,
  )
}
