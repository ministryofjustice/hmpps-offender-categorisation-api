package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Incident response object nested within the incident dto from prison API")
data class IncidentResponseDto(
  @Schema(description = "Question asked within the incident", example = INCIDENT_RESPONSE_QUESTION_SEXUAL_ASSAULT)
  val question: String,
  @Schema(description = "Answer to the asked question", example = INCIDENT_RESPONSE_ANSWER_YES)
  val answer: String,
) {
  companion object {
    const val INCIDENT_RESPONSE_QUESTION_SEXUAL_ASSAULT = "WAS THIS A SEXUAL ASSAULT"
    const val INCIDENT_RESPONSE_QUESTION_CONCUSSION = "WAS MEDICAL TREATMENT FOR CONCUSSION OR INTERNAL INJURIES REQUIRED"
    const val INCIDENT_RESPONSE_QUESTION_SEXUAL_SERIOUS_INJURY = "WAS A SERIOUS INJURY SUSTAINED"
    const val INCIDENT_RESPONSE_QUESTION_SEXUAL_RESULT_IN_HOSPITAL = "DID INJURIES RESULT IN DETENTION IN OUTSIDE HOSPITAL AS AN IN-PATIENT"

    const val INCIDENT_RESPONSE_ANSWER_YES = "YES"
  }
}
