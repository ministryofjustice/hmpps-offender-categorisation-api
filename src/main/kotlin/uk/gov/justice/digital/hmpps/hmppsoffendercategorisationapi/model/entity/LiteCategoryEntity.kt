package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.sql.Date
import java.time.ZonedDateTime

@Entity
@Table(name = "lite_category")
class LiteCategoryEntity(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "booking_id")
  val bookingId: Long = -1,

  val category: String,

  @Column(name = "supervisor_category")
  val supervisorCategory: String,

  @Column(name = "offender_no")
  val offenderNo: String,

  @Column(name = "prison_id")
  val prisonId: String,

  @Column(name = "created_date")
  val createdDate: ZonedDateTime? = null,

  @Column(name = "approved_date")
  val approvedDate: ZonedDateTime? = null,

  /**
   * REDACTED
   */
  @Column(name = "assessed_by")
  val assessedBy: String = "",

  /**
   * REDACTED
   */
  @Column(name = "approved_by")
  val approvedBy: String = "",

  @Column(name = "assessment_committee")
  val assessmentCommittee: String,

  @Column(name = "assessment_comment")
  val assessmentComment: String,

  @Column(name = "next_review_date")
  val nextReviewDate: Date,

  @Column(name = "placement_prison_id")
  val placementPrisonId: String,

  @Column(name = "approved_committee")
  val approvedCommittee: String,

  @Column(name = "approved_placement_prison_id")
  val approvedPlacementPrisonId: String,

  @Column(name = "approved_placement_comment")
  val approvedPlacementComment: String,

  @Column(name = "approved_comment")
  val approvedComment: String,
)
