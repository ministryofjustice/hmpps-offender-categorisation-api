package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.prisoner.Alert
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.prisoner.ConvictedOffencesResponse
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.prisoner.CurrentIncentive
import java.time.LocalDate

@Schema(description = "Prisoner")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Prisoner(
  val prisonerNumber: String? = null,
  val status: String? = null,
  val prisonId: String? = null,
  val restrictedPatient: Boolean,
  val prisonName: String,
  val releaseDate: LocalDate? = null,
  val category: String? = null,
  val currentIncentive: CurrentIncentive? = null,
  val sentenceStartDate: LocalDate? = null,
  val legalStatus: String? = null,
  val convictedOffencesResponse: ConvictedOffencesResponse? = null,
  val alerts: List<Alert>? = null,
) {
  val currentlyInPrison: Boolean
    get() = status !== null && status == STATUS_ACTIVE_IN

  companion object {
    const val CATEGORY_B = "B"
    const val CATEGORY_C = "C"
    const val CATEGORY_R = "R"
    const val STATUS_ACTIVE_IN = "ACTIVE IN"
    const val STATUS_INACTIVE_OUT = "INACTIVE OUT"

    const val INCENTIVE_LEVEL_BASIC = "BAS"
    const val INCENTIVE_LEVEL_STANDARD = "STD"
    const val INCENTIVE_LEVEL_ENHANCED = "ENH"
    const val INCENTIVE_LEVEL_ENHANCED_TWO = "EN2"
    const val INCENTIVE_LEVEL_ENHANCED_THREE = "EN3"
  }
}
