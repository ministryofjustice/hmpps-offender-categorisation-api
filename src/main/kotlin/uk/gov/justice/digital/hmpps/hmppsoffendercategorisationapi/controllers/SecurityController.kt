package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.config.ErrorResponse
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.request.SecurityReviewRequest
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.FormService

@RestController
@Tag(name = "Security", description = "Add / update security input of categorisations")
@PreAuthorize("hasAnyRole('SYSTEM_USER')")
@Validated
@RequestMapping("/security", produces = [MediaType.APPLICATION_JSON_VALUE])
class SecurityController(
  private val formService: FormService,
) {
  @PostMapping("/review/{bookingId}")
  @Operation(
    summary = "Submit a security review",
    description = """Saves the security review for a specific categorisation""",
  )
  @ApiResponses(
    ApiResponse(
      responseCode = "200",
      description = "OK",
    ),
    ApiResponse(
      responseCode = "400",
      description = "Invalid request",
      content = [
        Content(
          mediaType = "application/json",
          schema = Schema(implementation = ErrorResponse::class),
        ),
      ],
    ),
    ApiResponse(
      responseCode = "401",
      description = "Unauthorised, requires a valid OAuth2 token",
      content = [
        Content(
          mediaType = "application/json",
          schema = Schema(implementation = ErrorResponse::class),
        ),
      ],
    ),
    ApiResponse(
      responseCode = "404",
      description = "Form record not found for booking ID",
      content = [
        Content(
          mediaType = "application/json",
          schema = Schema(implementation = ErrorResponse::class),
        ),
      ],
    ),
    ApiResponse(
      responseCode = "500",
      description = "Error",
      content = [
        Content(
          mediaType = "application/json",
          schema = Schema(implementation = ErrorResponse::class),
        ),
      ],
    ),
  )
  fun submitReview(
    @Parameter(
      description = "booking ID of the categorisation being reviewed",
      required = true,
    )
    @PathVariable
    bookingId: Long,
    @Parameter(
      description = "Details of the review being submitted.",
      required = true,
    )
    @Valid
    @RequestBody
    data: SecurityReviewRequest,
  ) = formService.saveSecurityReview(bookingId, data.userId, data.submitted, data.securityReview)
}
