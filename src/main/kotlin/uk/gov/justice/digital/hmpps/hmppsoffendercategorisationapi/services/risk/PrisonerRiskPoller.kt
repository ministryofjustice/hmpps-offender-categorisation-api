package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.risk

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonerSearchApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.PrisonerRiskProfileEntity
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.Prisoner
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.PrisonerRiskProfileRepository
import java.time.Clock
import java.time.ZonedDateTime

@Service
class PrisonerRiskPoller(
  private val prisonApiClient: PrisonApiClient,
  private val prisonerSearchApiClient: PrisonerSearchApiClient,
  private val prisonerRiskCalculator: PrisonerRiskCalculator,
  private val prisonerRiskProfileRepository: PrisonerRiskProfileRepository,
  private val clock: Clock,
) {

  fun pollPrisonersRisk() {
    val prisonIds = prisonApiClient.findPrisons().map { it.agencyId }

    prisonIds.forEach { prisonId: String ->
      pollPrisonersRisk(prisonId)
    }
  }

  private fun pollPrisonersRisk(prisonId: String) {
    var prisoners: List<Prisoner>
    var i = 0
    do {
      prisoners = prisonerSearchApiClient.findPrisonersByAgencyId(
        prisonId,
        i,
        PRISONERS_CHUNK_SIZE,
      )
      prisoners.forEach { prisoner ->
        val riskProfile = prisonerRiskCalculator.calculateRisk(prisoner.prisonerNumber!!)
        val jsonRiskProfile = jacksonObjectMapper().writeValueAsString(riskProfile)
        prisonerRiskProfileRepository.save(
          PrisonerRiskProfileEntity(
            offenderNo = prisoner.prisonerNumber,
            riskProfile = jsonRiskProfile,
            calculatedAt = ZonedDateTime.now(clock),
          ),
        )
      }
      i++
    } while (prisoners.count() >= PRISONERS_CHUNK_SIZE)
  }

  companion object {
    private const val PRISONERS_CHUNK_SIZE = 100
  }
}
