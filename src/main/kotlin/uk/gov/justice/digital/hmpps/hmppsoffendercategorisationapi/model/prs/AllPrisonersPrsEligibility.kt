package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.prs

import org.slf4j.LoggerFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.enum.PrsIneligibilityReason

class AllPrisonersPrsEligibility(
  val agencyId: String,
  var prisonersEligible: Int = 0,
  var reasonsForIneligibility: Map<PrsIneligibilityReason, Int> = mapOf(
    PrsIneligibilityReason.CATEGORY to 0,
    PrsIneligibilityReason.TIME_LEFT_TO_SERVE to 0,
    PrsIneligibilityReason.INCENTIVE_LEVEL to 0,
    PrsIneligibilityReason.ESCAPE to 0,
  ),
) {
  fun addPrisoner(prisonerPrsEligibility: PrisonerPrsEligibility) {
    if (prisonerPrsEligibility.isEligible) {
      prisonersEligible++
    }
    prisonerPrsEligibility.reasonForIneligibility.forEach {
      this.reasonsForIneligibility[it]?.inc()
    }
  }

  fun logResult() {
    val logStringBuilder = StringBuilder().append("PRS_ELIGIBILITY_INVESTIGATION: $agencyId, $prisonersEligible")
    this.reasonsForIneligibility.forEach {
      logStringBuilder.append(", ${it.value}")
    }
    log.info(logStringBuilder.toString())
  }

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}
