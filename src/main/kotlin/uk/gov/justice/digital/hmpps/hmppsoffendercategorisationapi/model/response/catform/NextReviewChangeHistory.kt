package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime
import java.util.Date

@Serializable
class NextReviewChangeHistory(

  val id: String?,

  @SerialName("booking_id")
  val bookingId: Long?,

  @SerialName("offender_no")
  val offenderNo: String?,

  // date
  @SerialName("next_review_date")
  val nextReviewDate: String?,

  val reason: String?,

  // datetime
  @SerialName("change_date")
  val changeDate: String?,
)