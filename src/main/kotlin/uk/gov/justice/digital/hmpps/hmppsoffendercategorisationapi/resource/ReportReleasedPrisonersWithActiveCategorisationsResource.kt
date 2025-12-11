package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.resource

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.FormService
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.ReleasedPrisonersWithActiveCategorisationService

@RestController
@Validated
@RequestMapping("/released-prisoners-with-active-categorisations", produces = [MediaType.APPLICATION_JSON_VALUE])
class ReportReleasedPrisonersWithActiveCategorisationsResource(
  private val releasedPrisonersWithActiveCategorisationService: ReleasedPrisonersWithActiveCategorisationService,
  private val formService: FormService,
) {
  @GetMapping("/report")
  @Operation(
    summary = "Report active categorisations for prisoners from prisoner search who have been released",
    description = """Calls prisoner search for each of the prisoners who have an active (e.g. non approved
      or cancelled) categorisation / recategorisation in progress to see if they have actually been released
      already""",
  )
  fun reportReleasedPrisonersWithActiveCategorisations() = releasedPrisonersWithActiveCategorisationService.report()

  @GetMapping("/test")
  @Operation(
    summary = "t",
    description = """t""",
  )
  fun test() = formService.cancelAnyInProgressReviewsDueToPrisonerRelease("G1668GA")

  @GetMapping("/update")
  @Operation(
    summary = "Report active categorisations for prisoners from prisoner search who have been released",
    description = """Calls prisoner search for each of the prisoners who have an active (e.g. non approved
      or cancelled) categorisation / recategorisation in progress to see if they have actually been released
      already""",
  )
  fun updateReleasedPrisonersWithActiveCategorisations() = releasedPrisonersWithActiveCategorisationService.update()
}
