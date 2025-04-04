package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services

import org.springframework.stereotype.Service

@Service
class PrisonerEventService(
  private val formService: FormService,
) {
  fun handleRelease(event: PrisonerEvent) {
    if (event.additionalInformation.reason == RELEASE_REASON) {
      formService.cancelAnyInProgressReviewsDueToPrisonerRelease(event.additionalInformation.nomsNumber)
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
