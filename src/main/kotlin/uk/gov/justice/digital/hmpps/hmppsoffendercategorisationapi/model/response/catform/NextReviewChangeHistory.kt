package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import java.time.ZonedDateTime
import java.util.Date

class NextReviewChangeHistory(

  val id: Long = -1,

  val bookingId: Long,

  val nextReviewDate: Date,

  val reason: String,

  val changeDate: ZonedDateTime? = null,

  val changedBy: String = "",
)