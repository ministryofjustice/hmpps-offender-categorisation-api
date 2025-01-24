package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "Security review (Request)")
@JsonIgnoreProperties(ignoreUnknown = true)
data class SecurityReviewRequest(
  @Schema(description = "Submitting security review, otherwise just saving the value")
  val submitted: Boolean = false,
  @Schema(description = "User ID of reviewer", example = "EXAMPLE_GEN")
  @field:NotBlank(message = "User ID must be provided")
  val userId: String,
  @Schema(
    description = "Security information",
    example = "Security information held on this prisoner, which could be relevant to their security categorisation",
    requiredMode = Schema.RequiredMode.NOT_REQUIRED,
  )
  @field:Size(max = 50000)
  val securityReview: String?,
)
