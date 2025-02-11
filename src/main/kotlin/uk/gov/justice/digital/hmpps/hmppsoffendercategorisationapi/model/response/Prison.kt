package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Prison")
class Prison(
  val agencyId: String,
  val description: String,
  val active: Boolean,
)
