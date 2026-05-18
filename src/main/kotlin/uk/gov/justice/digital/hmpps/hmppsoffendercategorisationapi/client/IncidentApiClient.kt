package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentReport
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentReportResponse
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

@Service
class IncidentApiClient(
  @Qualifier("incidentApiWebClient") private val webClient: WebClient,
) {

  fun getTotalNumberOfIncidents(prisonerNumber: String): Long = webClient.get()
    .uri { uriBuilder ->
      uriBuilder
        .path("/incident-reports")
        .queryParam("involvingPrisonerNumber", prisonerNumber)
        .queryParam("type", "ASSAULT_1", "ASSAULT_5")
        .queryParam(
          "status",
          "AWAITING_REVIEW",
          "ON_HOLD",
          "NEEDS_UPDATING",
          "UPDATED",
          "CLOSED",
          "WAS_CLOSED",
        )
        .queryParam("size", "1")
        .build()
    }
    .retrieve()
    .bodyToMono(object : ParameterizedTypeReference<IncidentReportResponse>() {})
    .block()!!.totalElements

  fun getIncidentIds(
    prisonerNumber: String,
    recentAssaultMonths: Long,
    size: Long,
  ): List<UUID> = webClient.get()
    .uri { uriBuilder ->
      uriBuilder
        .path("/incident-reports")
        .queryParam("involvingPrisonerNumber", prisonerNumber)
        .queryParam("type", "ASSAULT_1", "ASSAULT_5")
        .queryParam(
          "status",
          "AWAITING_REVIEW",
          "ON_HOLD",
          "NEEDS_UPDATING",
          "UPDATED",
          "CLOSED",
          "WAS_CLOSED",
        )
        .queryParam(
          "incidentDateFrom",
          LocalDate.now().minusMonths(recentAssaultMonths).format(DateTimeFormatter.ISO_LOCAL_DATE),
        )
        .queryParam("size", size.toString())
        .build()
    }
    .retrieve()
    .bodyToMono(object : ParameterizedTypeReference<IncidentReportResponse>() {})
    .block()!!.content.map { it.id }

  fun getDetailedIncidentReport(incidentId: UUID): IncidentReport = webClient.get()
    .uri { uriBuilder ->
      uriBuilder
        .path("/incident-reports/$incidentId/with-details")
        .build()
    }
    .retrieve()
    .bodyToMono(object : ParameterizedTypeReference<IncidentReport>() {})
    .block()!!
}
