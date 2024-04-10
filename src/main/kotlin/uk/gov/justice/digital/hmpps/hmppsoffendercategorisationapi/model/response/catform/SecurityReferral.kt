package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import kotlinx.serialization.SerialName

class SecurityReferral(

  val id: String?,

  @SerialName("offender_no")
  val offenderNo: String,

  @SerialName("offender_no")
  val prisonId: String?,

  val status: String?,

  @SerialName("raised_date")
  val raisedDate: String?,

  @SerialName("processed_date")
  val processedDate: String,
)
