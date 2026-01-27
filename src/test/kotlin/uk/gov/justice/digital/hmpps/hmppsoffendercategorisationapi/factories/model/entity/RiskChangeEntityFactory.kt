package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.model.entity

import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.RiskChangeEntity

class RiskChangeEntityFactory {

  private var offenderNo: String = "A1234BC"
  private var prisonId: String = "MDI"
  private var status: String = RiskChangeEntity.STATUS_NEW
  private var oldProfile: String? = null
  private var newProfile: String? = null
  private var raisedDate: java.time.ZonedDateTime? = null

  fun withOffenderNo(offenderNo: String): RiskChangeEntityFactory {
    this.offenderNo = offenderNo
    return this
  }

  fun withPrisonId(prisonId: String): RiskChangeEntityFactory {
    this.prisonId = prisonId
    return this
  }

  fun withStatus(status: String): RiskChangeEntityFactory {
    this.status = status
    return this
  }

  fun withOldProfile(oldProfile: String?): RiskChangeEntityFactory {
    this.oldProfile = oldProfile
    return this
  }

  fun withNewProfile(newProfile: String?): RiskChangeEntityFactory {
    this.newProfile = newProfile
    return this
  }

  fun withRaisedDate(raisedDate: java.time.ZonedDateTime?): RiskChangeEntityFactory {
    this.raisedDate = raisedDate
    return this
  }

  fun build() = RiskChangeEntity(
    offenderNo = this.offenderNo,
    prisonId = this.prisonId,
    status = this.status,
    oldProfile = this.oldProfile,
    newProfile = this.newProfile,
    raisedDate = this.raisedDate,
  )
}
