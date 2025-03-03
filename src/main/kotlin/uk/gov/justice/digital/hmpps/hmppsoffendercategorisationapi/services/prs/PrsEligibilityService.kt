package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.prs

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.ManageOffencesApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonerSearchApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.prs.AllPrisonersPrsEligibility
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.Prisoner

@Service
class PrsEligibilityService(
  private val prisonerSearchApiClient: PrisonerSearchApiClient,
  private val prisonApiClient: PrisonApiClient,
  private val manageOffencesApiClient: ManageOffencesApiClient,
) {
  fun report() {
    val allPrisons = prisonApiClient.findPrisons()
    allPrisons.forEach {
      reportPrisonerEligibilityForPrison(it.agencyId)
    }
  }

  private fun reportPrisonerEligibilityForPrison(agencyId: String) {
    val allPrisonersPrsEligibility = AllPrisonersPrsEligibility(agencyId)
    var prisoners: List<Prisoner>
    var i = 0
    do {
      prisoners = prisonerSearchApiClient.findPrisonersByAgencyId(agencyId, i, PRISONERS_CHUNK_SIZE)
      val sdsExcludedOffenceCodes = manageOffencesApiClient.checkWhichOffenceCodesAreSdsExcluded(getAllOffenceCodes(prisoners))
      prisoners.forEach {
        allPrisonersPrsEligibility.addPrisoner(
          (PrisonerPrsEligibilityCalculator(it, sdsExcludedOffenceCodes)).calculate(),
        )
      }
      i++
    } while (prisoners.count() >= PRISONERS_CHUNK_SIZE)
    allPrisonersPrsEligibility.logResult()
  }

  private fun getAllOffenceCodes(prisoners: List<Prisoner>): List<String> {
    val offenceCodes = mutableListOf<String>()
    prisoners.forEach { prisoner ->
      prisoner.allConvictedOffences?.map { it.offenceCode }?.let { offenceCodes.addAll(it) }
    }
    return offenceCodes.distinct()
  }

  companion object {
    private const val PRISONERS_CHUNK_SIZE = 100
  }
}
