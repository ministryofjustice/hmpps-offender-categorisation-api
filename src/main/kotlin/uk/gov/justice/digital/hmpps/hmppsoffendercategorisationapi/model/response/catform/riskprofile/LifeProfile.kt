package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.riskprofile

import kotlinx.serialization.Serializable

@Serializable
data class LifeProfile(
  val life: Boolean,
  val nomsId: String,
  val riskType: String,
  val provisionalCategorisation: String
)
