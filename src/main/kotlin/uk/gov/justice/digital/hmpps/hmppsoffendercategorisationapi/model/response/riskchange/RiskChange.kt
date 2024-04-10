package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange

import java.time.ZonedDateTime

data class RiskChange (
  val id:Long,
  val oldProfile: Profile?,
  val newProfile: Profile?,
  val offenderNo: String,
  val userId: String,
  val prisonId: String,
  val status: String,
  val raisedDate: ZonedDateTime
)