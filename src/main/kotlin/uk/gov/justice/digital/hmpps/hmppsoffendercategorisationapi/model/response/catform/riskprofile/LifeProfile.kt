package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.riskprofile

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class LifeProfile(
  private val life: Boolean? = null,

  val nomsId: String? = null,

  val provisionalCategorisation: String? = null,

  private val riskType: String? = null,
) {
  val servingACourtIssuedLifeSentence: Boolean?
    get() = this.life
}
