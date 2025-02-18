package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.prs

import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.enum.PrsIneligibilityReason

class PrisonerPrsEligibility(
  val reasonForIneligibility: List<PrsIneligibilityReason>,
) {
  val isEligible: Boolean
    get() = reasonForIneligibility.isEmpty()
}
