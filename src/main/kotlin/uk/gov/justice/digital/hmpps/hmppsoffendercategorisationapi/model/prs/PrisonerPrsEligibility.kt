package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.prs

import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.enum.PrsIneligibilityReason

class PrisonerPrsEligibility(
  val reasonForIneligibility: List<PrsIneligibilityReason>,
  val sdsExcludedOffenceCodes: List<String>,
) {
  val isEligible: Boolean
    get() = reasonForIneligibility.isEmpty()

  val isEligibleIncludingSdsExclusions: Boolean
    get() = this.isEligible && sdsExcludedOffenceCodes.isEmpty()
}
