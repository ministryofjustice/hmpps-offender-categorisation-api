package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange

import kotlinx.serialization.Serializable
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.common.RedactedSection

@Serializable
data class Profile(

  val soc: RedactedSection = RedactedSection(),

  val escape: Escape? = null,

  val violence: Violence? = null,

  val extremism: RedactedSection = RedactedSection(),
)
