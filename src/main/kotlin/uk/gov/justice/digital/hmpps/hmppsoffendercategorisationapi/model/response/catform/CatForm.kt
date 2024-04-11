package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class CatForm(

  val id: String?,

  @SerialName("form_response")
  val formResponse: Map<String,JsonElement>,

  @SerialName("booking_id")
  val bookingId: String?,

  val status: String?,

  @SerialName("referred_date")
  // datetime
  val referredDate: String?,

  @SerialName("sequence_no")
  val sequenceNo: String?,

  @SerialName("risk_profile")
  val riskProfile: RiskProfile?,

  @SerialName("prison_id")
  val prisonId: String?,

  @SerialName("offender_no")
  val offenderNo: String?,

  // datetime
  @SerialName("start_date")
  val startDate: String?,

  // datetime
  @SerialName("security_reviewed_date")
  val securityReviewedDate: String?,

  // datetime
  @SerialName("approval_date")
  val approvalDate: String?,

  @SerialName("cat_type")
  val catType: String?,

  @SerialName("nomis_sequence_no")
  val nomisSequenceNo: String?,

  // date
  @SerialName("assessment_date")
  val assessmentDate: String?,

  @SerialName("review_reason")
  val reviewReason: String?,

  // date
  @SerialName("due_by_date")
  val dueByDate: String?,

  // date
  @SerialName("cancelled_date")
  val cancelledDate: String?
)
