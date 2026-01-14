package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.risk

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonerSearchApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.PrisonerRiskProfileEntity
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.Prisoner
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.Prisoner.Companion.CATEGORY_C
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.Prisoner.Companion.CATEGORY_D
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.Prisoner.Companion.CATEGORY_J
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.risk.PrisonerRiskProfile
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
    log.info("Starting poll of prisoner risk profiles")
    val prisonIds = prisonApiClient.findPrisons().map { it.agencyId }

    prisonIds.forEach { prisonId: String ->
      pollPrisonersRisk(prisonId)
    }
    log.info("finished poll of prisoner risk profiles")
  }

  private fun pollPrisonersRisk(prisonId: String) {
    log.info("Polling prisoners risk profiles for prison $prisonId")
    var prisoners: List<Prisoner>
    var i = 0
    do {
      prisoners = prisonerSearchApiClient.findPrisonersByAgencyId(
        prisonId,
        i,
        PRISONERS_CHUNK_SIZE,
      )
      prisoners.forEach { prisoner ->
        try {
          val riskProfile = prisonerRiskCalculator.calculateRisk(prisoner.prisonerNumber!!)
          val jsonRiskProfile = jacksonObjectMapper().writeValueAsString(riskProfile)
          compareRiskProfiles(prisoner, riskProfile)
          prisonerRiskProfileRepository.save(
            PrisonerRiskProfileEntity(
              offenderNo = prisoner.prisonerNumber,
              riskProfile = jsonRiskProfile,
              calculatedAt = ZonedDateTime.now(clock),
            ),
          )
        } catch (e: Exception) {
          log.error("Error calculating risk profile for prisoner ${prisoner.prisonerNumber}", e)
        }
      }
      i++
    } while (prisoners.count() >= PRISONERS_CHUNK_SIZE)
  }

  private fun compareRiskProfiles(prisoner: Prisoner, newRiskProfile: PrisonerRiskProfile) {
    val existingRiskProfile = prisonerRiskProfileRepository.findByOffenderNo(prisoner.prisonerNumber!!)
    if (existingRiskProfile != null) {
      val existingRiskProfileObj = jacksonObjectMapper().readValue(existingRiskProfile.riskProfile, PrisonerRiskProfile::class.java)
      if (
        existingRiskProfileObj.escapeRiskAlerts != newRiskProfile.escapeRiskAlerts ||
        existingRiskProfileObj.escapeListAlerts != newRiskProfile.escapeListAlerts ||
        (!existingRiskProfileObj.riskDueToViolence && newRiskProfile.riskDueToViolence) ||
        (!existingRiskProfileObj.riskDueToSeriousOrganisedCrime && newRiskProfile.riskDueToSeriousOrganisedCrime)
      ) {
        if (listOf(CATEGORY_C, CATEGORY_D, CATEGORY_J).contains(prisoner.category)) {
          log.info("Risk profile changed for prisoner ${prisoner.prisonerNumber} in category ${prisoner.category}. existing = $existingRiskProfileObj, new = $newRiskProfile")
        }
      }
    }
  }

  companion object {
    private const val PRISONERS_CHUNK_SIZE = 100
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}
