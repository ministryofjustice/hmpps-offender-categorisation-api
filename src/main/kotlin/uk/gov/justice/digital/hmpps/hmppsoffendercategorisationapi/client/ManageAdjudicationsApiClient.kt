package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.AdjudicationsSummary

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
}
