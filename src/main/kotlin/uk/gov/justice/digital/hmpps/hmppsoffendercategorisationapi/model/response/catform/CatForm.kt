package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CatForm(
  val id: String? = null,

  @JsonProperty("form_response")
  val formResponse: Map<String, Any>? = null,

  val status: String? = null,

  @JsonProperty("referred_date")
  val referredDate: String? = null,

  @JsonProperty("risk_profile")
  val riskProfile: RiskProfile? = null,

  @JsonProperty("prison_id")
  val prisonId: String? = null,

  @JsonProperty("offender_no")
  val offenderNo: String? = null,

  @JsonProperty("start_date")
  val startDate: String? = null,

  @JsonProperty("security_reviewed_date")
  val securityReviewedDate: String? = null,

  @JsonProperty("approval_date")
  val approvalDate: String? = null,

  @JsonProperty("cat_type")
  val catType: String? = null,

  @JsonProperty("assessment_date")
  val assessmentDate: String? = null,

  @JsonProperty("review_reason")
  val reviewReason: String? = null,

  @JsonProperty("due_by_date")
  val dueByDate: String? = null,

  @JsonProperty("cancelled_date")
  val cancelledDate: String? = null,
)
