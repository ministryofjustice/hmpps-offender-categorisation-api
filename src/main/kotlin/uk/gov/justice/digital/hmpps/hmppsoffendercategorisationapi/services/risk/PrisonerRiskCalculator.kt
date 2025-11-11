package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.risk

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonerAlertsApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentDto
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentResponseDto
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.risk.EscapeAlert
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.risk.PrisonerRiskProfile
import uk.gov.justice.digital.hmpps.riskprofiler.dto.prisonerAlert.PrisonerAlertResponseDto.Companion.ALERT_CODE_ESCAPE_LIST
import uk.gov.justice.digital.hmpps.riskprofiler.dto.prisonerAlert.PrisonerAlertResponseDto.Companion.ALERT_CODE_ESCAPE_LIST_HEIGHTENED
import uk.gov.justice.digital.hmpps.riskprofiler.dto.prisonerAlert.PrisonerAlertResponseDto.Companion.ALERT_CODE_ESCAPE_RISK
import uk.gov.justice.digital.hmpps.riskprofiler.dto.prisonerAlert.PrisonerAlertResponseDto.Companion.ALERT_CODE_OCGM
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZonedDateTime

@Service
class PrisonerRiskCalculator(
  val prisonerAlertsApiClient: PrisonerAlertsApiClient,
  val prisonApiClient: PrisonApiClient,
  val viperService: ViperService,
  val clock: Clock,
) {

  fun calculateRisk(prisonerNumber: String) {
    val alerts = prisonerAlertsApiClient.findPrisonerAlerts(
      prisonerNumber,
      listOf(ALERT_CODE_ESCAPE_RISK, ALERT_CODE_ESCAPE_LIST, ALERT_CODE_ESCAPE_LIST_HEIGHTENED, ALERT_CODE_OCGM),
    )
    val assaultIncidents = prisonApiClient.getAssaultIncidents(prisonerNumber)
    val viperData = viperService.getViperData(prisonerNumber)

    val prisonerRiskProfile = PrisonerRiskProfile(
      alerts.filter { it.alertCode.code == ALERT_CODE_ESCAPE_RISK }.map { EscapeAlert.mapFromDto(it, clock) }.toList(),
      alerts.filter { it.alertCode.code == ALERT_CODE_ESCAPE_LIST || it.alertCode.code == ALERT_CODE_ESCAPE_LIST_HEIGHTENED }.map { EscapeAlert.mapFromDto(it, clock) }.toList(),
      !alerts.filter { it.alertCode.code == ALERT_CODE_OCGM }.isEmpty,
      viperData.aboveThreshold || numberOfAssaultIncidentsConsideredARisk(assaultIncidents),
    )
  }

  private fun numberOfAssaultIncidentsConsideredARisk(assaultIncidents: List<IncidentDto>): Boolean {
    val nonDuplicateAssaultIncidents = assaultIncidents.filter { it.incidentStatus != IncidentDto.INCIDENT_STATUS_DUP }
    if (nonDuplicateAssaultIncidents.length > 5) {
      return true
    }
    val recentNonDuplicateSeriousAssaults = nonDuplicateAssaultIncidents
      .filter { LocalDateTime.parse(it.reportTime).isAfter(ZonedDateTime.now(clock).minusMonths(RECENT_ASSAULT_MONTHS).toLocalDateTime()) }
      .count { incident: IncidentDto ->
        incident.responses.any { response: IncidentResponseDto ->
          IncidentDto.SERIOUS_ASSAULT_QUESTIONS.contains(response.question) && response.answer == IncidentDto.QUESTION_ANSWER_YES
        }
      }
    return recentNonDuplicateSeriousAssaults > 0
  }

  companion object {
    const val RECENT_ASSAULT_MONTHS = 6L
  }
}
