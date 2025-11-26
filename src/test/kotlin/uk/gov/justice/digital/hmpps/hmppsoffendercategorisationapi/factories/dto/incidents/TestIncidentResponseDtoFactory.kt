package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.dto.incidents

import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentResponseDto
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentResponseDto.Companion.INCIDENT_RESPONSE_ANSWER_YES
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentResponseDto.Companion.INCIDENT_RESPONSE_QUESTION_SEXUAL_ASSAULT

class TestIncidentResponseDtoFactory {
  private var question: String = INCIDENT_RESPONSE_QUESTION_SEXUAL_ASSAULT
  private var answer: String = INCIDENT_RESPONSE_ANSWER_YES

  fun withQuestion(question: String): TestIncidentResponseDtoFactory {
    this.question = question
    return this
  }

  fun withAnswer(answer: String): TestIncidentResponseDtoFactory {
    this.answer = answer
    return this
  }

  fun build() = IncidentResponseDto(
    question = this.question,
    answer = this.answer,
  )
}
