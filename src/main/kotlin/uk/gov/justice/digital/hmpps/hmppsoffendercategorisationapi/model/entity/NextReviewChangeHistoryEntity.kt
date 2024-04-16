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
@Table(name = "next_review_change_history")
class NextReviewChangeHistoryEntity(

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = -1,

  @Column(name = "offender_no")
  val offenderNo: String,

  @Column(name = "booking_id")
  val bookingId: String,

  @Column(name = "next_review_date")
  val nextReviewDate: Date,

  val reason: String,

  @Column(name = "change_date")
  val changeDate: ZonedDateTime? = null,

  /**
   * REDACTED
   */
  @Column(name = "changed_by")
  val changedBy: String = "",
)
