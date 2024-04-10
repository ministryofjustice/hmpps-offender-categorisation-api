package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import com.nimbusds.jose.shaded.gson.JsonObject
import io.hypersistence.utils.hibernate.type.json.JsonType
import kotlinx.serialization.Serializable
import org.hibernate.annotations.Type
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.riskprofile.LifeProfile
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.common.RedactedSection
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Escape
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Violence

@Serializable
data class RiskProfile(
  val history: RedactedSection?,

  val offences: List<String>? = emptyList(),

  val socProfile: RedactedSection?,

  val lifeProfile: LifeProfile,

  val escapeProfile: Escape,

  val violenceProfile: Violence,

  val extremismProfile: RedactedSection?,
)


