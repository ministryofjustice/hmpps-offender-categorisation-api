package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class LiteCategory(
  val category: String,

  val supervisorCategory: String? = null,

  @JsonProperty("offender_no")
  val offenderNo: String? = null,

  @JsonProperty("prison_id")
  val prisonId: String? = null,

  // datetime
  @JsonProperty("created_date")
  val createdDate: String? = null,

  // datetime
  @JsonProperty("approved_date")
  val approvedDate: String? = null,

  @JsonProperty("assessment_committee")
  val assessmentCommittee: String? = null,

  @JsonProperty("assessment_comment")
  val assessmentComment: String? = null,

  // date
  @JsonProperty("next_review_date")
  val nextReviewDate: String? = null,

  @JsonProperty("placement_prison_id")
  val placementPrisonId: String? = null,

  @JsonProperty("approved_committee")
  val approvedCommittee: String? = null,

  @JsonProperty("approved_placement_prison_id")
  val approvedPlacementPrisonId: String? = null,

  @JsonProperty("approved_placement_comment")
  val approvedPlacementComment: String? = null,

  @JsonProperty("approved_comment")
  val approvedComment: String? = null,
  val sequence: String,
)
