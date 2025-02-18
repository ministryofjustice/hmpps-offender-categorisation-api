package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.adjudication

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "AdjudicationsSummary")
@JsonInclude(JsonInclude.Include.NON_NULL)
class AdjudicationsSummary(
  val bookingId: Int,
  val adjudicationCount: Int,
)
