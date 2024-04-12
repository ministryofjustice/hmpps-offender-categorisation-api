package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.riskprofile

import kotlinx.serialization.Serializable

@Serializable
data class LifeProfile(
  val life: Boolean? = null,
  val nomsId: String? = null,
  val riskType: String? = null,
  val provisionalCategorisation: String? = null,
)
