package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import java.time.ZonedDateTime
import java.util.Date

class CatForm(
  val id: Long = -1,
  val formResponse: FormResponse,
  val bookingId: String,
  val status: String,
  val referredDateTime: ZonedDateTime,
  val sequenceNo: String,

  val riskProfile: RiskProfile,

  val prisonId: String,
  val offenderNo: String,
  val startDate: ZonedDateTime,
  val securityReviewedDate: ZonedDateTime,
  val approvalDate: ZonedDateTime,
  val catType: String,
  val nomisSequenceNo: String,
  val assessmentDate: Date,
  val reviewReason: Date,
  val dueByDate: Date,
  val cancelledDate: Date
)
