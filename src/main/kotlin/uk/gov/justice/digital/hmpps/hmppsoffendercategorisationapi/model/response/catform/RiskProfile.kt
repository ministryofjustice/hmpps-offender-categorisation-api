package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.riskprofile.LifeProfile
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.common.RedactedSection
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Escape
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Violence

@Serializable
data class RiskProfile(
  val history: RedactedSection? = null,

  val offences: List<Map<String, JsonElement>>? = emptyList(),

  val socProfile: RedactedSection? = null,

  val lifeProfile: LifeProfile? = null,

  val escapeProfile: Escape? = null,

  val violenceProfile: Violence? = null,

  val extremismProfile: RedactedSection? = null,
)