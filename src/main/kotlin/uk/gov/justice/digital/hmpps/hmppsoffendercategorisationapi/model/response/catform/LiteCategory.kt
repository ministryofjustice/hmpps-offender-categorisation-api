package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import java.time.ZonedDateTime
import java.util.*

data class LiteCategory(
  val bookingId: Long = -1,

  val category: String,

  val supervisorCategory: String,

  val offenderNo: String,

  val prisonId: String,

  val createdDate: ZonedDateTime? = null,

  val approvedDate: ZonedDateTime? = null,

  val assessmentCommittee: String,

  val assessmentComment: String,

  val nextReviewDate: Date,

  val placementPrisonId: String,

  val approvedCommittee: String,

  val approvedPlacementPrisonId: String,

  val approvedPlacementComment: String,

  val approvedComment: String,
)
