package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SecurityReferral(

  val id: String? = null,

  @SerialName("offender_no")
  val offenderNo: String,

  @SerialName("prison_id")
  val prisonId: String?,

  val status: String?,

  @SerialName("raised_date")
  val raisedDate: String?,

  @SerialName("processed_date")
  val processedDate: String,
)
