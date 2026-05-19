package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.risk

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.IncidentApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonerAlertsApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentDto
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentResponseDto
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentResponseDto.Companion.INCIDENT_RESPONSE_ANSWER_YES
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentResponseDto.Companion.INCIDENT_RESPONSE_QUESTION_CONCUSSION
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentResponseDto.Companion.INCIDENT_RESPONSE_QUESTION_RESULT_IN_HOSPITAL
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentResponseDto.Companion.INCIDENT_RESPONSE_QUESTION_SERIOUS_INJURY
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentResponseDto.Companion.INCIDENT_RESPONSE_QUESTION_SEXUAL_ASSAULT
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.prisonerAlert.PrisonerAlertResponseDto
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.prisonerAlert.PrisonerAlertResponseDto.Companion.ALERT_CODE_ESCAPE_LIST
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.prisonerAlert.PrisonerAlertResponseDto.Companion.ALERT_CODE_ESCAPE_LIST_HEIGHTENED
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.prisonerAlert.PrisonerAlertResponseDto.Companion.ALERT_CODE_ESCAPE_RISK
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.prisonerAlert.PrisonerAlertResponseDto.Companion.ALERT_CODE_OCGM
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.risk.EscapeAlert
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.risk.PrisonerRiskProfile
import java.time.Clock
import java.time.ZonedDateTime

@Service
class PrisonerRiskCalculator(
  val prisonerAlertsApiClient: PrisonerAlertsApiClient,
  val incidentApiClient: IncidentApiClient,
  val viperService: ViperService,
  val clock: Clock,
) {

  fun calculateRisk(prisonerNumber: String): PrisonerRiskProfile {
    val alerts = prisonerAlertsApiClient.findPrisonerAlerts(
      prisonerNumber,
      listOf(ALERT_CODE_ESCAPE_RISK, ALERT_CODE_ESCAPE_LIST, ALERT_CODE_ESCAPE_LIST_HEIGHTENED, ALERT_CODE_OCGM),
    )
    val numberOfIncidents = incidentApiClient.getTotalNumberOfIncidents(prisonerNumber)

    val assaultIncidents = if (numberOfIncidents > 0) {
      getAssaultIncidents(
        prisonerNumber = prisonerNumber,
        recentAssaultMonths = RECENT_ASSAULT_MONTHS,
        size = numberOfIncidents,
      )
    } else {
      emptyList()
    }

    val viperData = viperService.getViperData(prisonerNumber)

    return PrisonerRiskProfile(
      alerts.filter { it.alertCode.code == ALERT_CODE_ESCAPE_RISK && alertIsActiveAndNotExpired(it) }.map { EscapeAlert.mapFromDto(it, clock) }.toList(),
      alerts.filter { (it.alertCode.code == ALERT_CODE_ESCAPE_LIST || it.alertCode.code == ALERT_CODE_ESCAPE_LIST_HEIGHTENED) && alertIsActiveAndNotExpired(it) }.map { EscapeAlert.mapFromDto(it, clock) }.toList(),
      !alerts.none { it.alertCode.code == ALERT_CODE_OCGM && alertIsActiveAndNotExpired(it) },
      viperData.aboveThreshold && numberOfAssaultIncidentsConsideredARisk(numberOfIncidents, assaultIncidents),
    )
  }

  fun getAssaultIncidents(prisonerNumber: String, recentAssaultMonths: Long = RECENT_ASSAULT_MONTHS, size: Long = 6): List<IncidentDto> {
    val incidentIds = incidentApiClient.getIncidentIds(prisonerNumber, recentAssaultMonths, size)

    return incidentIds.map { incidentId ->
      incidentApiClient.getDetailedIncidentReport(incidentId)
    }
      .map { incident ->
        IncidentDto(
          incidentType = incident.type,
          incidentStatus = incident.status,
          responses = incident.questions.map { question ->
            IncidentResponseDto(
              question = question.question,
              answer = question.responses.firstOrNull()?.response ?: "",
            )
          },
        )
      }
  }

  private fun numberOfAssaultIncidentsConsideredARisk(totalNumberOfIncidents: Long, assaultIncidents: List<IncidentDto>): Boolean {
    val recentNonDuplicateSeriousAssaults = assaultIncidents.count { incident: IncidentDto ->
      incident.responses.any { response: IncidentResponseDto ->
        listOf(
          INCIDENT_RESPONSE_QUESTION_SEXUAL_ASSAULT,
          INCIDENT_RESPONSE_QUESTION_CONCUSSION,
          INCIDENT_RESPONSE_QUESTION_SERIOUS_INJURY,
          INCIDENT_RESPONSE_QUESTION_RESULT_IN_HOSPITAL,
        ).contains(response.question.uppercase()) &&
          response.answer.uppercase() == INCIDENT_RESPONSE_ANSWER_YES
      }
    }
    return totalNumberOfIncidents >= MINIMUM_NUMBER_OF_ASSAULTS_TO_CONSIDER_RISK && recentNonDuplicateSeriousAssaults > 0
  }

  fun alertIsActiveAndNotExpired(alert: PrisonerAlertResponseDto): Boolean {
    val now = ZonedDateTime.now(clock).toLocalDate()
    return alert.active && (alert.activeTo == null || alert.activeTo.isAfter(now))
  }

  companion object {
    const val RECENT_ASSAULT_MONTHS = 6L
    const val MINIMUM_NUMBER_OF_ASSAULTS_TO_CONSIDER_RISK = 5
  }
}
