package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import com.fasterxml.jackson.annotation.JsonInclude
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.riskprofile.LifeProfile
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.common.RedactedSection
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Escape
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Violence

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RiskProfile(
  val catHistory: RedactedSection? = null,

  val history: RedactedSection? = null,

  val offences: List<Map<String, Any>>? = emptyList(),

  val socProfile: RedactedSection? = null,

  val lifeProfile: LifeProfile? = null,

  val escapeProfile: Escape? = null,

  val violenceProfile: Violence? = null,

  val extremismProfile: RedactedSection? = null,
)
