package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation

import com.fasterxml.jackson.module.kotlin.readValue
import com.vladmihalcea.hibernate.type.json.JsonType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import net.minidev.json.JSONObject
import org.hibernate.annotations.JdbcType
import org.hibernate.annotations.Type
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.enum.CatType
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.enum.ReviewReason
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.objectMapper
import java.time.LocalDate
import java.time.LocalDateTime

/**
 *  assigned_user_id, user_id referred_by, approved_by,
 *  assessed_by, cancelled_by all ** REACTED **
 */
@Entity
@Table(name = "form", schema = "public")
class FormEntity(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = 0,

  @Type(JsonType::class)
  @Column(columnDefinition = "jsonb", name = "form_response")
  private var formResponse: String? = null,

  /**
   * REDACTED
   */
  @Column(name = "booking_id")
  val bookingId: Long = 0,

  /**
   * REDACTED
   */
  @Column(name = "user_id")
  val userId: String = "",

  private var status: String,

  @Column(name = "referred_date")
  val referredDate: LocalDateTime? = null,

  /**
   * REDACTED
   */
  @Column(name = "sequence_no")
  val sequenceNo: Int = 1,

  @Type(JsonType::class)
  @Column(columnDefinition = "jsonb", name = "risk_profile")
  val riskProfile: String? = null,

  @Column(name = "prison_id")
  val prisonId: String,

  @Column(name = "offender_no")
  val offenderNo: String,

  @Column(name = "start_date")
  val startDate: LocalDateTime,

  @Column(name = "security_reviewed_date")
  private var securityReviewedDate: LocalDateTime?,

  @Column(name = "security_reviewed_by")
  private var securityReviewedBy: String?,

  @Column(name = "approval_date")
  val approvalDate: LocalDate?,

  @Enumerated(EnumType.STRING)
  @Column(name = "cat_type")
  @JdbcType(PostgreSQLEnumJdbcType::class)
  val catType: CatType,

  /**
   * REDACTED
   */
  @Column(name = "nomis_sequence_no")
  val nomisSequenceNo: Int? = null,

  @Column(name = "assessment_date")
  val assessmentDate: LocalDate?,

  @Enumerated(EnumType.STRING)
  @JdbcType(PostgreSQLEnumJdbcType::class)
  @Column(name = "review_reason")
  val reviewReason: ReviewReason,

  @Column(name = "due_by_date")
  val dueByDate: LocalDate?,

  @Column(name = "cancelled_date")
  private var cancelledDate: LocalDateTime?,

  /**
   * REDACTED
   */
  @Column(name = "cancelled_by")
  val cancelledBy: String,
) {
  fun getFormResponse(): String? = formResponse
  fun getStatus(): String = status
  fun getSecurityReviewedDate(): LocalDateTime? = securityReviewedDate
  fun getSecurityReviewedBy(): String? = securityReviewedBy
  fun getCancelledDate(): LocalDateTime? = cancelledDate

  fun updateFormResponse(section: String, name: String, value: Map<String, String>) {
    val formResponseMap = formResponse?.let { objectMapper.readValue<MutableMap<String, Any>>(it) }
    if (formResponseMap != null) {
      formResponseMap[section] = mapOf(name to value)
      formResponse = JSONObject(formResponseMap).toString()
    }
  }
  fun setStatus(newStatus: String) {
    status = newStatus
  }
  fun setSecurityReviewedDate(newSecurityReviewedDate: LocalDateTime) {
    securityReviewedDate = newSecurityReviewedDate
  }
  fun setSecurityReviewedBy(newSecurityReviewedBy: String) {
    securityReviewedBy = newSecurityReviewedBy
  }

  fun setCancelledDate(newCancelledDate: LocalDateTime) {
    cancelledDate = newCancelledDate
  }

  companion object {
    const val STATUS_APPROVED = "APPROVED"
    const val STATUS_CANCELLED = "CANCELLED"
    const val STATUS_STARTED = "STARTED"
    const val STATUS_SECURITY_BACK = "SECURITY_BACK"
    const val STATUS_SECURITY_AUTO = "SECURITY_AUTO"
    const val STATUS_SECURITY_MANUAL = "SECURITY_MANUAL"
    const val STATUS_SECURITY_FLAGGED = "SECURITY_FLAGGED"
    const val STATUS_SUPERVISOR_BACK = "SUPERVISOR_BACK"
    const val STATUS_AWAITING_APPROVAL = "AWAITING_APPROVAL"
    const val STATUS_CANCELLED_AFTER_RELEASE = "CANCELLED_RELEASE"

    const val FORM_RESPONSE_SECTION_SECURITY = "security"
    const val FORM_RESPONSE_FIELD_NAME = "review"
  }
}
