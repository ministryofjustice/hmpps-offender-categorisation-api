package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonerSearchApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.prs.AllPrisonersPrsEligibility
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.Prisoner

@Service
class PrsEligibilityService(
  private val prisonerSearchApiClient: PrisonerSearchApiClient,
) {
  fun report() {
  }

  fun reportPrisonerEligibilityForPrison(agencyId: String) {
    val allPrisonersPrsEligibility = AllPrisonersPrsEligibility(agencyId)
    var prisoners: List<Prisoner>
    var i = 0
    do {
      prisoners = prisonerSearchApiClient.findPrisonersByAgencyId(agencyId, i, PRISONERS_CHUNK_SIZE)
      prisoners.forEach {
        allPrisonersPrsEligibility.addPrisoner(
          (PrisonerPrsEligibilityCalculator(it).calculate()),
        )
      }
      i++
    } while (prisoners.count() >= PRISONERS_CHUNK_SIZE)
    allPrisonersPrsEligibility.logResult()
  }

  companion object {
    const val PRISONERS_CHUNK_SIZE = 100
  }
}
