package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.prisoner

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "ConvictedOffencesResponse")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ConvictedOffencesResponse(
  val allConvictedOffences: List<ConvictedOffence>,
)

@Schema(description = "ConvictedOffence")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ConvictedOffence(
  val offenceCode: String,
  val offenceDescription: String,
)
