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
  // datetime
  val referredDate: String? = null,

  @SerialName("sequence_no")
  val sequenceNo: String? = null,

  @SerialName("risk_profile")
  val riskProfile: RiskProfile? = null,

  @SerialName("prison_id")
  val prisonId: String? = null,

  @SerialName("offender_no")
  val offenderNo: String? = null,

  // datetime
  @SerialName("start_date")
  val startDate: String? = null,

  // datetime
  @SerialName("security_reviewed_date")
  val securityReviewedDate: String? = null,

  // datetime
  @SerialName("approval_date")
  val approvalDate: String? = null,

  @SerialName("cat_type")
  val catType: String? = null,

  @SerialName("nomis_sequence_no")
  val nomisSequenceNo: String? = null,

  // date
  @SerialName("assessment_date")
  val assessmentDate: String? = null,

  @SerialName("review_reason")
  val reviewReason: String? = null,

  // date
  @SerialName("due_by_date")
  val dueByDate: String? = null,

  // date
  @SerialName("cancelled_date")
  val cancelledDate: String? = null,
)
