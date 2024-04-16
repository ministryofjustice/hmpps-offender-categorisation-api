package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RiskChange(

  val id: String? = null,

  @SerialName("old_profile")
  val oldProfile: Profile? = null,

  @SerialName("new_profile")
  val newProfile: Profile? = null,

  @SerialName("offender_no")
  val offenderNo: String? = null,

  @SerialName("prison_id")
  val prisonId: String? = null,

  val status: String? = null,

  // datetime
  @SerialName("raised_date")
  val raisedDate: String? = null,
)
