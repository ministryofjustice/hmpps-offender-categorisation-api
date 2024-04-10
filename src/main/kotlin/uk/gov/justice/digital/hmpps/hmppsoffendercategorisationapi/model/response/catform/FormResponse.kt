package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import java.time.ZonedDateTime

class FormResponse(

  val id: Long = -1,

  val offenderNo: String,

  val userId: String = "",

  val prisonId: String,

  val status: String,

  val raisedDate: ZonedDateTime? = null,

  val processedDate: ZonedDateTime? = null,
)
