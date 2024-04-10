package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LiteCategory(
  val bookingId: Long = -1,

  val category: String,

  val supervisorCategory: String,

  @SerialName("offender_no")
  val offenderNo: String?,

  @SerialName("prison_id")
  val prisonId: String,

  // datetime
  @SerialName("created_date")
  val createdDate: String?,

  // datetime
  @SerialName("approved_date")
  val approvedDate: String?,

  @SerialName("assessment_committee")
  val assessmentCommittee: String,

  @SerialName("assessment_comment")
  val assessmentComment: String,

  // date
  @SerialName("next_review_date")
  val nextReviewDate: String?,

  @SerialName("placement_prison_id")
  val placementPrisonId: String,

  @SerialName("approved_committee")
  val approvedCommittee: String,

  @SerialName("approved_placement_prison_id")
  val approvedPlacementPrisonId: String,

  @SerialName("approved_placement_comment")
  val approvedPlacementComment: String,

  @SerialName("approved_comment")
  val approvedComment: String,
)
