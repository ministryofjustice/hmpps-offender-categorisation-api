package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.ZonedDateTime

@Serializable
data class CatForm(
  val id: String?,
  @SerialName("form_response")
  val formResponse: FormResponse?,
  @SerialName("booking_id")
  val bookingId: String?,
  val status: String?,
  // datetime
  val referredDateTime: String?,
  val sequenceNo: String?,

  val riskProfile: RiskProfile?,

  val prisonId: String?,
  val offenderNo: String?,
  // datetime
  val startDate: String?,
  // datetime
  val securityReviewedDate: String?,
  // datetime
  val approvalDate: String?,
  val catType: String?,
  val nomisSequenceNo: String?,
  // date
  val assessmentDate: String?,
  val reviewReason: String?,
  // date
  val dueByDate: String?,
  // date
  val cancelledDate: String?
)
