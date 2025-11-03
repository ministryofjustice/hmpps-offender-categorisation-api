package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.risk

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonerSearchApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.Prisoner

@Service
class PrisonerRiskPoller(
  private val prisonApiClient: PrisonApiClient,
  private val prisonerSearchApiClient: PrisonerSearchApiClient,
  private val prisonerRiskCalculator: PrisonerRiskCalculator,
) {

  fun pollPrisonersRisk() {
    val prisonIds = prisonApiClient.findPrisons().map { it.agencyId }

    prisonIds.forEach { prisonId: String ->
      pollPrisonersRisk(prisonId)
    }
  }

  fun pollPrisonersRisk(prisonId: String) {
    var prisoners: List<Prisoner>
    var i = 0
    do {
      prisoners = prisonerSearchApiClient.findPrisonersByAgencyId(
        prisonId,
        i,
        PRISONERS_CHUNK_SIZE,
      )
      prisoners.forEach { prisoner ->
        prisonerRiskCalculator.calculateRisk(prisoner.prisonerNumber!!)
      }
      i++
    } while (prisoners.count() >= PRISONERS_CHUNK_SIZE)
  }

  companion object {
    private const val PRISONERS_CHUNK_SIZE = 100
  }
}
