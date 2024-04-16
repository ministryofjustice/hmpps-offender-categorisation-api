package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class CatForm(
  val id: String? = null,

  @SerialName("form_response")
  val formResponse: Map<String, JsonElement>? = null,

  @SerialName("booking_id")
  val bookingId: String? = null,

  val status: String? = null,

  @SerialName("referred_date")
  val referredDate: String? = null,

  @SerialName("sequence_no")
  val sequenceNo: String? = null,

  @SerialName("risk_profile")
  val riskProfile: RiskProfile? = null,

  @SerialName("prison_id")
  val prisonId: String? = null,

  @SerialName("offender_no")
  val offenderNo: String? = null,

  @SerialName("start_date")
  val startDate: String? = null,

  @SerialName("security_reviewed_date")
  val securityReviewedDate: String? = null,

  @SerialName("approval_date")
  val approvalDate: String? = null,

  @SerialName("cat_type")
  val catType: String? = null,

  @SerialName("nomis_sequence_no")
  val nomisSequenceNo: String? = null,

  @SerialName("assessment_date")
  val assessmentDate: String? = null,

  @SerialName("review_reason")
  val reviewReason: String? = null,

  @SerialName("due_by_date")
  val dueByDate: String? = null,

  @SerialName("cancelled_date")
  val cancelledDate: String? = null,
)
