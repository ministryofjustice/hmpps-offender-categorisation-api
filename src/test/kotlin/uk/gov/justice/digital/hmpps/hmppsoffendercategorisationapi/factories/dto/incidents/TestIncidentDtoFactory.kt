package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.dto.incidents

import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentDto
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentResponseDto

class TestIncidentDtoFactory {
  private var incidentStatus: String = "CLOSED"
  private var responses: List<IncidentResponseDto> = emptyList()

  fun withResponses(responses: List<IncidentResponseDto>): TestIncidentDtoFactory {
    this.responses = responses
    return this
  }

  fun withIncidentStatus(incidentStatus: String): TestIncidentDtoFactory {
    this.incidentStatus = incidentStatus
    return this
  }

  fun build(): IncidentDto = IncidentDto(
    incidentType = "ASSAULT_5",
    incidentStatus = this.incidentStatus,
    responses = this.responses,
  )
}
