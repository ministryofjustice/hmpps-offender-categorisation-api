package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "SdsExcludedOffenceCode")
class SdsExcludedOffenceCode(
  val offenceCode: String,
  val schedulePart: String,
)
