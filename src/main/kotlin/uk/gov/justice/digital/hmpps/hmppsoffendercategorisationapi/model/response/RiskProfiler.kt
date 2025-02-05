package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.common.RedactedSection
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Escape
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Soc
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Violence

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RiskProfiler(

  @JsonProperty("offender_no")
  val offenderNo: String? = null,
  val escape: Escape? = null,

  /**
   * Redacted - removed from response payload
   */
  private val extremism: RedactedSection? = RedactedSection(),

  private val soc: Soc? = null,
  val violence: Violence? = null,
  val dateAndTimeRiskInformationLastUpdated: String? = null,
) {
  // removing any reference to the word 'soc' but leaving in the actual data because it is not soc specific
  val transferToSecurity: Boolean?
    get() = if (this.soc == null) null else this.soc.transferToSecurity
  val provisionalCategorisation: String?
    get() = if (this.soc == null) null else this.soc.provisionalCategorisation
}
