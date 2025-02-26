package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.prs

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.AssessRisksAndNeedsApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.ManageAdjudicationsApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.ManageOffencesApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonerSearchApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.ProbationSearchApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.prs.AllPrisonersPrsEligibility
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.Prisoner
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.RiskSummary

@Service
class PrsEligibilityService(
  private val prisonerSearchApiClient: PrisonerSearchApiClient,
  private val prisonApiClient: PrisonApiClient,
  private val manageOffencesApiClient: ManageOffencesApiClient,
  private val probationSearchApiClient: ProbationSearchApiClient,
  private val assessRisksAndNeedsApiClient: AssessRisksAndNeedsApiClient,
  private val manageAdjudicationsApiClient: ManageAdjudicationsApiClient,
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
      val roshScores = getAllRoshScores(prisoners)
      val prisonersWithAdjudications = getAllAdjudications(prisoners)
      prisoners.forEach {
        allPrisonersPrsEligibility.addPrisoner(
          (
            PrisonerPrsEligibilityCalculator(
              it,
              sdsExcludedOffenceCodes,
              roshScores[it.prisonerNumber],
              prisonersWithAdjudications.contains(it.prisonerNumber),
            )
            ).calculate(),
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

  private fun getAllRoshScores(prisoners: List<Prisoner>): Map<String, RiskSummary> {
    val prisonerNumbers = prisoners.mapNotNull { it.prisonerNumber }
    if (prisonerNumbers.isEmpty()) {
      return emptyMap()
    }
    val crns = probationSearchApiClient.findProbationPersonsFromPrisonNumbers(prisonerNumbers).associate { it.otherIds.nomsNumber to it.otherIds.crn }
    val roshSummaries = mutableMapOf<String, RiskSummary>()
    crns.forEach {
      if (it.key !== null) {
        assessRisksAndNeedsApiClient.findRiskSummary(it.value)
          ?.let { roshSummary -> roshSummaries[it.key!!] = roshSummary }
      }
    }
    return roshSummaries
  }

  private fun getAllAdjudications(prisoners: List<Prisoner>): List<String> {
    val bookingIds = prisoners.associate { it.bookingId to it.prisonerNumber }
    val prisonerNumbersWithAdjudications = mutableListOf<String>()
    bookingIds.forEach {
      if (it.key !== null) {
        val adjudicationsSummary = manageAdjudicationsApiClient.findAdjudicationsByBookingId(it.key!!)
        if (adjudicationsSummary?.adjudicationCount !== null && adjudicationsSummary.adjudicationCount > 0 && it.value !== null) {
          prisonerNumbersWithAdjudications.add(it.value!!)
        }
      }
    }
    return prisonerNumbersWithAdjudications
  }

  companion object {
    private const val PRISONERS_CHUNK_SIZE = 100
  }
}
