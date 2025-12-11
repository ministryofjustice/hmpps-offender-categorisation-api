package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.Prison
import java.time.LocalDate

@Service
class PrisonApiClient(
  @Qualifier("prisonApiWebClient") private val webClient: WebClient,
) {
  fun findPrisons(): List<Prison> = webClient.get()
    .uri("/api/agencies/prisons")
    .retrieve()
    .bodyToMono(object : ParameterizedTypeReference<List<Prison>>() {})
    .block()!!

  fun rejectPendingCategorisations(bookingId: Long, sequenceNumber: Int, evaluationDate: LocalDate, reviewCommitteeCode: String) = webClient.put()
    .uri("/api/offender-assessments/category/reject")
    .bodyValue(
      mapOf(
        "bookingId" to bookingId,
        "assessmentSeq" to sequenceNumber,
        "evaluationDate" to evaluationDate,
        "reviewCommitteeCode" to reviewCommitteeCode,
        "committeeCommentText" to "test",
      ),
    )
    .retrieve()
    .bodyToMono(String::class.java)
    .block()

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}
