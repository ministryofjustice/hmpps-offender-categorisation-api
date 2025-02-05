package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import com.fasterxml.jackson.annotation.JsonInclude
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.riskprofile.LifeProfile
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.common.RedactedSection
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Escape
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Soc
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Violence

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RiskProfile(
  val catHistory: List<Map<String, Any>>? = null,

  val history: RedactedSection? = null,

  val offences: List<Map<String, Any>>? = emptyList(),

  private val socProfile: Soc? = null,

  private val lifeProfile: LifeProfile? = null,

  val escapeProfile: Escape? = null,

  val violenceProfile: Violence? = null,

  private val extremismProfile: RedactedSection? = null,
) {
  val courtIssuedLifeSentence: LifeProfile?
    get() = this.lifeProfile

  // removing any reference to the word 'soc' but leaving in the actual data because it is not soc specific
  val transferToSecurity: Boolean?
    get() = if (this.socProfile == null) null else this.socProfile.transferToSecurity
  val provisionalCategorisation: String?
    get() = if (this.socProfile == null) null else this.socProfile.provisionalCategorisation
}
