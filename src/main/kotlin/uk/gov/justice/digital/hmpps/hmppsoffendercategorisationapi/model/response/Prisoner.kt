package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Prisoner")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Prisoner(
  val prisonerNumber: String? = null,
  val status: String? = null,
  val prisonId: String? = null,
  val restrictedPatient: Boolean,
) {
  val currentlyInPrison: Boolean
    get() = status !== null && status == STATUS_ACTIVE_IN

  companion object {
    const val STATUS_ACTIVE_IN = "ACTIVE IN"
    const val STATUS_INACTIVE_OUT = "INACTIVE OUT"
  }
}
