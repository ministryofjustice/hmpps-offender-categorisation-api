package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.adjudication

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Adjudication")
@JsonInclude(JsonInclude.Include.NON_NULL)
class Adjudication(
  val prisonerNumber: String,
  val status: String,
  val createdDateTime: String,
) {
  companion object {
    const val STATUS_APPROVED = "APPROVED"
    const val STATUS_CHARGE_PROVEN = "CHARGE_PROVEN"
  }
}
