package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
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
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.FormService

@RestController
@Tag(name = "Test", description = "test controller")
@PreAuthorize("hasAnyRole('SYSTEM_USER')")
@Validated
@RequestMapping("/test", produces = [MediaType.APPLICATION_JSON_VALUE])
class TestController(
  val formService: FormService,
) {
  @GetMapping("/release/{prisonerNumber}")
  @Operation(
    summary = "Test trigger a cancel due to release",
    description = """triggers a cancellation of any in-progress reviews due to prisoner release""",
  )
  @ApiResponses(
    ApiResponse(
      responseCode = "200",
      description = "OK",
    ),
  )
  fun triggerTestCancellation(
    @Parameter(
      description = "prisoner number",
      required = true,
    )
    @PathVariable
    prisonerNumber: String,
  ) = formService.cancelAnyInProgressReviewsDueToPrisonerRelease(prisonerNumber)
}
