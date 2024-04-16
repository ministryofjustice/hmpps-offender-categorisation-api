package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class NextReviewChangeHistory(
  val id: String? = null,

  @SerialName("booking_id")
  val bookingId: String? = null,

  @SerialName("offender_no")
  val offenderNo: String? = null,

  // date
  @SerialName("next_review_date")
  val nextReviewDate: String? = null,

  val reason: String? = null,

  // datetime
  @SerialName("change_date")
  val changeDate: String? = null,
)