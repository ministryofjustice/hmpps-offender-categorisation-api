package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.common.RedactedSection
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Violence

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RiskProfiler(

  @JsonProperty("offender_no")
  val offenderNo: String? = null,

  /**
   * Redacted - empty json definition
   */
  val escape: RedactedSection? = RedactedSection(),

  /**
   * Redacted - empty json definition
   */
  val extremism: RedactedSection? = RedactedSection(),

  /**
   * Redacted - empty json definition
   */
  val soc: RedactedSection? = RedactedSection(),

  val violence: Violence? = null,

  // datetime
  @JsonProperty("execute_date_time")
  val executeDateTime: String? = null,
)
