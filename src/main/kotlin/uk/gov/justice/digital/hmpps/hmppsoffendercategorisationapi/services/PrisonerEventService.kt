package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonerSearchApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.Prisoner.Companion.STATUS_INACTIVE_OUT

@Service
class PrisonerEventService(
  private val formService: FormService,
  private val prisonerSearchApiClient: PrisonerSearchApiClient,
) {
  fun handleRelease(event: PrisonerEvent) {
    if (event.additionalInformation.reason == RELEASE_REASON) {
      val nomsNumber = event.additionalInformation.nomsNumber
      val prisoner = prisonerSearchApiClient.findPrisoner(nomsNumber)
      if (prisoner.status == STATUS_INACTIVE_OUT) {
        formService.cancelAnyInProgressReviewsDueToPrisonerRelease(nomsNumber)
      }
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
