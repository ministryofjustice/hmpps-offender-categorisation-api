package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange

import com.fasterxml.jackson.annotation.JsonInclude
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.common.RedactedSection

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Profile(
  private val soc: Soc? = null,

  val escape: Escape? = null,

  val violence: Violence? = null,

  private val extremism: RedactedSection? = null,
) {
  // removing any reference to the word 'soc' but leaving in the actual data because it is not soc specific
  val transferToSecurity: Boolean?
    get() = if (this.soc == null) null else this.soc.transferToSecurity
  val provisionalCategorisation: String?
    get() = if (this.soc == null) null else this.soc.provisionalCategorisation
}
