package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.dto.incidents

import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentDto
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentResponseDto

class TestIncidentDtoFactory {
  private var incidentStatus: String = IncidentDto.INCIDENT_STATUS_DUP
  private var reportTime: String = "2020-01-01T10:00:00"
  private var responses: List<IncidentResponseDto> = emptyList()

  fun withResponses(responses: List<IncidentResponseDto>): TestIncidentDtoFactory {
    this.responses = responses
    return this
  }

  fun withIncidentStatus(incidentStatus: String): TestIncidentDtoFactory {
    this.incidentStatus = incidentStatus
    return this
  }

  fun withReportTime(reportTime: String): TestIncidentDtoFactory {
    this.reportTime = reportTime
    return this
  }

  fun build(): IncidentDto = IncidentDto(
    incidentStatus = this.incidentStatus,
    reportTime = this.reportTime,
    responses = this.responses,
  )
}
