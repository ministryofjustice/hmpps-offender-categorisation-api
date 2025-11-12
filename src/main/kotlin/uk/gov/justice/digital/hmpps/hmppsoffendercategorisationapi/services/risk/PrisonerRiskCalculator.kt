package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.risk

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonerAlertsApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentDto
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentResponseDto
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
  val viperService: ViperService,
  val clock: Clock,
) {

  fun calculateRisk(prisonerNumber: String): PrisonerRiskProfile {
    val alerts = prisonerAlertsApiClient.findPrisonerAlerts(
      prisonerNumber,
      listOf(ALERT_CODE_ESCAPE_RISK, ALERT_CODE_ESCAPE_LIST, ALERT_CODE_ESCAPE_LIST_HEIGHTENED, ALERT_CODE_OCGM),
    )
    val assaultIncidents = prisonApiClient.getAssaultIncidents(prisonerNumber)
    val viperData = viperService.getViperData(prisonerNumber)

    return PrisonerRiskProfile(
      alerts.filter { it.alertCode.code == ALERT_CODE_ESCAPE_RISK }.map { EscapeAlert.mapFromDto(it, clock) }.toList(),
      alerts.filter { it.alertCode.code == ALERT_CODE_ESCAPE_LIST || it.alertCode.code == ALERT_CODE_ESCAPE_LIST_HEIGHTENED }.map { EscapeAlert.mapFromDto(it, clock) }.toList(),
      !alerts.filter { it.alertCode.code == ALERT_CODE_OCGM }.isEmpty,
      viperData.aboveThreshold || numberOfAssaultIncidentsConsideredARisk(assaultIncidents),
    )
  }

  private fun numberOfAssaultIncidentsConsideredARisk(assaultIncidents: List<IncidentDto>): Boolean {
    val nonDuplicateAssaultIncidents = assaultIncidents.filter { it.incidentStatus != IncidentDto.INCIDENT_STATUS_DUP }
    if (nonDuplicateAssaultIncidents.count() > 5) {
      return true
    }
    val recentNonDuplicateSeriousAssaults = nonDuplicateAssaultIncidents
      .filter { LocalDateTime.parse(it.reportTime).isAfter(ZonedDateTime.now(clock).minusMonths(RECENT_ASSAULT_MONTHS).toLocalDateTime()) }
      .count { incident: IncidentDto ->
        incident.responses.any { response: IncidentResponseDto ->
          listOf(
            IncidentDto.INCIDENT_RESPONSE_QUESTION_SEXUAL_ASSAULT,
            IncidentDto.INCIDENT_RESPONSE_QUESTION_MEDICAL_TREATMENT_CONCUSSION_INTERNAL_INJURIES,
            IncidentDto.INCIDENT_RESPONSE_QUESTION_SERIOUS_INJURY_SUSTAINED,
            IncidentDto.INCIDENT_RESPONSE_QUESTION_INJURIES_RESULTED_IN_DETENTION_IN_OUTSIDE_HOSPITAL_AS_INPATIENT,
          ).contains(response.question) &&
            response.answer == IncidentDto.QUESTION_ANSWER_YES
        }
      }
    return recentNonDuplicateSeriousAssaults > 0
  }

  companion object {
    const val RECENT_ASSAULT_MONTHS = 6L
  }
}
