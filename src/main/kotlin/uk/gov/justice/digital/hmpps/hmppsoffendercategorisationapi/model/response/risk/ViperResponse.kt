package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.risk

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

@Schema(description = "Viper data about a prisoner")
@JsonIgnoreProperties(ignoreUnknown = true)
data class ViperResponse(
  @Schema(description = "prisoner number") val prisonerNumber: String,
  @Schema(description = "viper score") val score: BigDecimal? = null,
)
