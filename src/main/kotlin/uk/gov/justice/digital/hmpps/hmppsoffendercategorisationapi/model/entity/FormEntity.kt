package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity

import com.vladmihalcea.hibernate.type.json.JsonType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.Type

/**
 *  assigned_user_id, user_id referred_by, approved_by,
 *  assessed_by, security_reviewed_by cancelled_by all ** REACTED **
 */
@Entity
@Table(name = "form")
class FormEntity(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = -1,

  @Type(JsonType::class)
  @Column(columnDefinition = "jsonb", name = "form_response")
  val formResponse: String? = null,

  @Column(name = "booking_id")
  val bookingId: Long = -1,

  /**
   * REDACTED
   */
  @Column(name = "user_id")
  val userId: String = "",

  val status: String,

  @Column(name = "referred_date")
  val referredDate: String = "",

  @Column(name = "sequence_no")
  val sequenceNo: String = "",

  @Type(JsonType::class)
  @Column(columnDefinition = "jsonb", name = "risk_profile")
  val riskProfile: String? = null,

  @Column(name = "prison_id")
  val prisonId: String,

  @Column(name = "offender_no")
  val offenderNo: String,

  @Column(name = "start_date")
  val startDate: String,

  @Column(name = "security_reviewed_date")
  val securityReviewedDate: String,

  @Column(name = "approval_date")
  val approvalDate: String,

  @Column(name = "cat_type")
  val catType: String,

  @Column(name = "nomis_sequence_no")
  val nomisSequenceNo: String,

  @Column(name = "assessment_date")
  val assessmentDate: String,

  @Column(name = "review_reason")
  val reviewReason: String,

  @Column(name = "due_by_date")
  val dueByDate: String,

  @Column(name = "cancelled_date")
  val cancelledDate: String,

  @Column(name = "cancelled_by")
  val cancelledBy: String,
)
