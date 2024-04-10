package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RiskChange (

  val id: String,

  @SerialName("old_profile")
  val oldProfile: Profile?,

  @SerialName("new_profile")
  val newProfile: Profile?,

  @SerialName("offender_no")
  val offenderNo: String,

  @SerialName("prison_id")
  val prisonId: String,

  val status: String,

  // datetime
  @SerialName("raised_date")
  val raisedDate: String
)