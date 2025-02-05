package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.FormEntity
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.enum.CatType
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.enum.ReviewReason

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CatForm(
  val id: String? = null,

  @JsonProperty("form_response")
  val formResponse: Map<String, Any>? = null,

  private val status: String? = null,

  @JsonProperty("referred_date")
  val referredDate: String? = null,

  @JsonProperty("risk_profile")
  val riskProfile: RiskProfile? = null,

  val prisonId: String? = null,

  @JsonProperty("offender_no")
  val offenderNo: String? = null,

  @JsonProperty("start_date")
  val startDate: String? = null,

  @JsonProperty("security_reviewed_date")
  val securityReviewedDate: String? = null,

  @JsonProperty("approval_date")
  val approvalDate: String? = null,

  private val catType: CatType? = null,

  @JsonProperty("assessment_date")
  val assessmentDate: String? = null,

  private val reason: ReviewReason? = null,

  @JsonProperty("due_by_date")
  val dueByDate: String? = null,

  @JsonProperty("cancelled_date")
  val cancelledDate: String? = null,
) {
  val reviewReason: String?
    get() = if (this.reason == null) {
      null
    } else {
      when (this.reason) {
        ReviewReason.MANUAL -> "Review manually started"
        ReviewReason.AGE -> "Age change - review required"
        ReviewReason.DUE -> "Review due"
        ReviewReason.RISK_CHANGE -> "Change in risk"
      }
    }

  val typeOfCategorisationAssessment: String?
    get() = if (this.catType == null) {
      null
    } else {
      when (this.catType) {
        CatType.INITIAL -> "Initial"
        CatType.RECAT -> "Recategorisation"
      }
    }

  val reviewStatus: String?
    get() = if (this.status == null) {
      null
    } else {
      when (this.status) {
        FormEntity.STATUS_SECURITY_BACK -> "Security review complete"
        FormEntity.STATUS_CANCELLED -> "Review cancelled"
        FormEntity.STATUS_APPROVED -> "Review approved"
        FormEntity.STATUS_SECURITY_AUTO -> "Automatically referred to security team"
        FormEntity.STATUS_SECURITY_MANUAL -> "Manually referred to security team"
        FormEntity.STATUS_SECURITY_FLAGGED -> "Flagged to be referred to security team"
        FormEntity.STATUS_STARTED -> "Review started"
        FormEntity.STATUS_SUPERVISOR_BACK -> "Review back from supervisor"
        FormEntity.STATUS_AWAITING_APPROVAL -> "Review awaiting approval"
        else -> this.status
      }
    }
}
