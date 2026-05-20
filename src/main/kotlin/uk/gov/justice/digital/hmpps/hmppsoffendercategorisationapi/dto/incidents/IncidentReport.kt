package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID

open class IncidentReportResponse(
  val content: List<ReportBasic>,
  val totalElements: Long,
)

@Schema(description = "Incident report with only key information")
@JsonInclude(JsonInclude.Include.NON_NULL)
open class ReportBasic(
  val id: UUID,
)

@Schema(description = "Incident report with all related information")
class IncidentReport(
  id: UUID,
  val type: String,
  val status: String,
  val questions: List<Question>,
  val prisonersInvolved: List<PrisonerInvolvement>,
) : ReportBasic(
  id = id,
)

@Schema(description = "Question with responses making up an incident report")
data class Question(
  val code: String,
  val question: String,
  val responses: List<Response>,
)

@Schema(description = "Response to a question making up an incident report")
data class Response(
  val code: String,
  val response: String,
)

@Schema(description = "Prisoner involved in an incident")
data class PrisonerInvolvement(
  val prisonerNumber: String,
  val prisonerRole: String,
)
