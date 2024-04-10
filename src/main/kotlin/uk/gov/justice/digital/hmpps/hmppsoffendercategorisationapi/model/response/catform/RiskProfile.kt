package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import io.hypersistence.utils.hibernate.type.json.JsonType
import org.hibernate.annotations.Type
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.riskprofile.LifeProfile
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Escape
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Violence

data class RiskProfile(

  @Type(JsonType::class)
  val history: String?,

  val offences: List<String> = emptyList(),

  @Type(JsonType::class)
  val socProfile: String?,

  val lifeProfile: LifeProfile,

  val escapeProfile: Escape,

  val violenceProfile: Violence,

  @Type(JsonType::class)
  val extremismProfile: String
)


