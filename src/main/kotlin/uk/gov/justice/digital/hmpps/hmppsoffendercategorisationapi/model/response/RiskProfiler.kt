package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.common.RedactedSection
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Violence

@Serializable
data class RiskProfiler(

  @SerialName("offender_no")
  val offenderNo: String,

  /**
   * Redacted - empty json definition
   */
  val escape: RedactedSection?,

  /**
   * Redacted - empty json definition
   */
  val extremism: RedactedSection?,

  /**
   * Redacted - empty json definition
   */
  val soc: RedactedSection?,

  val violence: Violence?,

  // datetime
  @SerialName("execute_date_time")
  val executeDateTime: String,
)
