package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.adjudication.Adjudication
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.adjudication.AdjudicationsSummary

@Service
class ManageAdjudicationsApiClient(
  @Qualifier("manageAdjudicationsApiWebClient") private val webClient: WebClient,
) {
  fun findAdjudicationsByBookingId(bookingId: Int): AdjudicationsSummary? {
    return webClient.get()
      .uri("/adjudications/by-booking-id/$bookingId")
      .retrieve()
      .bodyToMono(object : ParameterizedTypeReference<AdjudicationsSummary>() {})
      .block()
  }

  fun findAdjudicationsByAgencyId(
    agencyId: String,
    startDate: String,
    statuses: List<String>,
    page: Int,
    size: Int = 20,
  ): List<Adjudication>? {
    return webClient.get()
      .uri { uriBuilder ->
        uriBuilder
          .path("/reported-adjudications/reports")
          .queryParam("startDate", startDate)
          .queryParam("status", statuses)
          .queryParam("page", statuses)
          .queryParam("size", size)
          .build()
      }
      .header("Active-Caseload", agencyId)
      .retrieve()
      .bodyToMono(object : ParameterizedTypeReference<PageableResult>() {})
      .block()
      ?.content
  }
}

data class PageableResult(
  val content: List<Adjudication>,
)
