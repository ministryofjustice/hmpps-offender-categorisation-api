package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.resource

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.risk.PrisonerRiskPoller

@RestController
@Validated
@RequestMapping("/prisoner-risk-calculation", produces = [MediaType.APPLICATION_JSON_VALUE])
class PrisonerRiskCalculation(
  private val prisonerRiskPoller: PrisonerRiskPoller,
) {
  @GetMapping("/poll-prisons")
  @Operation(
    summary = "Calculate risk profile for all prisoners at all prisons",
    description = """Loops through all prisoners at all prisons and calculates a fresh risk profile, 
      saving that profile in the database.
    """,
  )
  fun pollPrisoners() = prisonerRiskPoller.pollPrisonersRisk()
}
