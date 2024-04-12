package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LiteCategory(
  val bookingId: Long = -1,

  val category: String,

  val supervisorCategory: String? = null,

  @SerialName("offender_no")
  val offenderNo: String? = null,

  @SerialName("prison_id")
  val prisonId: String? = null,

  // datetime
  @SerialName("created_date")
  val createdDate: String? = null,

  // datetime
  @SerialName("approved_date")
  val approvedDate: String? = null,

  @SerialName("assessment_committee")
  val assessmentCommittee: String? = null,

  @SerialName("assessment_comment")
  val assessmentComment: String? = null,

  // date
  @SerialName("next_review_date")
  val nextReviewDate: String? = null,

  @SerialName("placement_prison_id")
  val placementPrisonId: String? = null,

  @SerialName("approved_committee")
  val approvedCommittee: String? = null,

  @SerialName("approved_placement_prison_id")
  val approvedPlacementPrisonId: String? = null,

  @SerialName("approved_placement_comment")
  val approvedPlacementComment: String? = null,

  @SerialName("approved_comment")
  val approvedComment: String? = null,
  val sequence: String,
)
