package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class PrisonerEventService(
) {

  private companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }
  fun handleRelease(event: PrisonerEvent) {
    if (event.additionalInformation.reason == RELEASE_REASON) {
      log.info("Handling release of ${event.additionalInformation.nomsNumber}")
    }
  }
}

const val RELEASE_REASON = "RELEASED"

data class PrisonerEvent(
  val additionalInformation: PrisonerAdditionalInformation,
)
data class PrisonerAdditionalInformation(
  val nomsNumber: String,
  val reason: String,
)
