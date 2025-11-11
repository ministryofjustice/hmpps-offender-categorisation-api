package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Incident dto from prison API")
data class IncidentDto(
  @Schema(description = "Status of the incident", example = INCIDENT_STATUS_DUP)
  val incidentStatus: String,
  @Schema(description = "Date time when the incident was reported", example = "2018-02-11T08:00:00")
  val reportTime: String,
  @Schema(description = "List of questions and answers about the incident")
  val responses: List<IncidentResponseDto>,
) {
  companion object {
    const val INCIDENT_TYPE_ASSAULT = "ASSAULT"
    const val INCIDENT_TYPE_ASSAULTS3 = "ASSAULTS3"

    const val PARTICIPATION_ROLE_ACTINV = "ACTINV"
    const val PARTICIPATION_ROLE_ASSIAL = "ASSIAL"
    const val PARTICIPATION_ROLE_FIGHT = "FIGHT"
    const val PARTICIPATION_ROLE_IMPED = "IMPED"
    const val PARTICIPATION_ROLE_PERP = "PERP"
    const val PARTICIPATION_ROLE_SUSASS = "SUSASS"
    const val PARTICIPATION_ROLE_SUSINV = "SUSINV"

    const val INCIDENT_STATUS_DUP = "DUP"

    const val INCIDENT_RESPONSE_QUESTION_SEXUAL_ASSAULT = "WAS THIS A SEXUAL ASSAULT"
    const val INCIDENT_RESPONSE_QUESTION_MEDICAL_TREATMENT_CONCUSSION_INTERNAL_INJURIES =
      "WAS MEDICAL TREATMENT FOR CONCUSSION OR INTERNAL INJURIES REQUIRED"
    const val INCIDENT_RESPONSE_QUESTION_SERIOUS_INJURY_SUSTAINED = "WAS A SERIOUS INJURY SUSTAINED"
    const val INCIDENT_RESPONSE_QUESTION_INJURIES_RESULTED_IN_DETENTION_IN_OUTSIDE_HOSPITAL_AS_INPATIENT =
      "DID INJURIES RESULT IN DETENTION IN OUTSIDE HOSPITAL AS AN IN-PATIENT"

    const val SERIOUS_ASSAULT_QUESTIONS = listOf(
      "WAS THIS A SEXUAL ASSAULT",
      "WAS MEDICAL TREATMENT FOR CONCUSSION OR INTERNAL INJURIES REQUIRED",
      "WAS A SERIOUS INJURY SUSTAINED",
      "DID INJURIES RESULT IN DETENTION IN OUTSIDE HOSPITAL AS AN IN-PATIENT",
    )

    const val QUESTION_ANSWER_YES = "YES"
  }
}
