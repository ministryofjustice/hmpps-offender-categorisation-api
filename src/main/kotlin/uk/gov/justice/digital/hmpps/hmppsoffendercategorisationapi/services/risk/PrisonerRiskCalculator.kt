package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.risk

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonApiClient
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
import java.time.LocalDateTime
import java.time.ZonedDateTime

@Service
class PrisonerRiskCalculator(
  val prisonerAlertsApiClient: PrisonerAlertsApiClient,
  val prisonApiClient: PrisonApiClient,
  val clock: Clock,
) {

  fun calculateRisk(prisonerNumber: String): PrisonerRiskProfile {
    val alerts = prisonerAlertsApiClient.findPrisonerAlerts(
      prisonerNumber,
      listOf(ALERT_CODE_ESCAPE_RISK, ALERT_CODE_ESCAPE_LIST, ALERT_CODE_ESCAPE_LIST_HEIGHTENED, ALERT_CODE_OCGM),
    )
    val assaultIncidents = prisonApiClient.getAssaultIncidents(prisonerNumber)

    return PrisonerRiskProfile(
      alerts.filter { it.alertCode.code == ALERT_CODE_ESCAPE_RISK && alertIsActiveAndNotExpired(it) }.map { EscapeAlert.mapFromDto(it, clock) }.toList(),
      alerts.filter { (it.alertCode.code == ALERT_CODE_ESCAPE_LIST || it.alertCode.code == ALERT_CODE_ESCAPE_LIST_HEIGHTENED) && alertIsActiveAndNotExpired(it) }.map { EscapeAlert.mapFromDto(it, clock) }.toList(),
      !alerts.filter { it.alertCode.code == ALERT_CODE_OCGM && alertIsActiveAndNotExpired(it) }.isEmpty(),
      numberOfAssaultIncidentsConsideredARisk(assaultIncidents),
    )
  }

  private fun numberOfAssaultIncidentsConsideredARisk(assaultIncidents: List<IncidentDto>): Boolean {
    val nonDuplicateAssaultIncidents = assaultIncidents.filter { it.incidentStatus != IncidentDto.INCIDENT_STATUS_DUP }
    val recentNonDuplicateSeriousAssaults = nonDuplicateAssaultIncidents
      .filter { LocalDateTime.parse(it.reportTime).isAfter(ZonedDateTime.now(clock).minusMonths(RECENT_ASSAULT_MONTHS).toLocalDateTime()) }
      .count { incident: IncidentDto ->
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
    return nonDuplicateAssaultIncidents.count() >= MINIMUM_NUMBER_OF_ASSAULTS_TO_CONSIDER_RISK && recentNonDuplicateSeriousAssaults > 0
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
