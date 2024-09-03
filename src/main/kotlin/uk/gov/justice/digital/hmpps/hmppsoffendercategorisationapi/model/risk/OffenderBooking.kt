package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.risk

import com.fasterxml.jackson.annotation.JsonInclude
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
data class OffenderBooking(
  val bookingId: Long? = null,
  val bookingNo: String? = null,
  val offenderNo: String? = null,
  val imprisonmentStatus: String? = null,
) : Serializable
