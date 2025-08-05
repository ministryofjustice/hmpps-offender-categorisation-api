package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.config.ErrorResponse
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.risk.ViperService

@RestController
@Tag(name = "Risk", description = "Load data about prisoner risk")
@PreAuthorize("hasAnyRole('SYSTEM_USER')")
@Validated
@RequestMapping("/risk", produces = [MediaType.APPLICATION_JSON_VALUE])
class RiskController(
  private val viperService: ViperService,
) {
  @GetMapping("/viper/{prisonerNumber}")
  @Operation(
    summary = "Get prisoner viper score if present",
    description = """returns the viper score of a prisoner if it is present""",
  )
  @ApiResponses(
    ApiResponse(
      responseCode = "200",
      description = "OK",
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
      description = "No viper score for prisoner",
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
  fun getViperScore(
    @Parameter(
      description = "prisoner number",
      required = true,
    )
    @PathVariable
    prisonerNumber: String,
  ) = viperService.getViperScore(prisonerNumber)
}
