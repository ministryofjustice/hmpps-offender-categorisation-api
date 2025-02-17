package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.resource

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.prs.PrsEligibilityService

@RestController
@Validated
@RequestMapping("/prs-eligibility", produces = [MediaType.APPLICATION_JSON_VALUE])
class PrsEligibility(
  private val prsEligibilityService: PrsEligibilityService,
) {
  @GetMapping("/report")
  @Operation(
    summary = "Report PRS eligibility for investigation purposes",
    description = """Checks all prisoners at all prisons to see what number would be eligible for PRS
      | using the criteria which is currently understood.
    """,
  )
  fun reportPrsEligibility() =
    prsEligibilityService.report()
}
