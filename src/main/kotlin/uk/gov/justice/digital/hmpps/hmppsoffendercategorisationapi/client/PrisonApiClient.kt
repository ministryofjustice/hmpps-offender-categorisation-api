package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class PrisonApiClient(
  @Qualifier("prisonApiWebClient") private val webClient: WebClient,
) {
  fun setPendingCategorisationsInactive(bookingId: Long) {
    webClient.put()
      .uri("/api/offender-assessments/category/$bookingId/inactive?status=PENDING")
      .retrieve()
      .toBodilessEntity()
      .doOnSuccess {
        log.info("Set pending categorisations inactive for booking {}", bookingId)
      }
      .doOnError {
        log.error("Failed to set pending categorisations inactive for booking {}", bookingId, it)
      }
      .block()
  }

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}
