package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.prisoner

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "CurrentIncentive")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class CurrentIncentive(
  val level: Level,
)

@Schema(description = "Level")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Level(
  val code: String,
  val description: String,
)
