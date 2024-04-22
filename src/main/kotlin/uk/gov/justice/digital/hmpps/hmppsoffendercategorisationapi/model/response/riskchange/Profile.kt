package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange

import com.fasterxml.jackson.annotation.JsonInclude
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.common.RedactedSection

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Profile(
  val soc: RedactedSection = RedactedSection(),

  val escape: Escape? = null,

  val violence: Violence? = null,

  val extremism: RedactedSection = RedactedSection(),
)
