package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.ManageAdjudicationsApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonerSearchApiClient

@Service
class PrsEligibilityService(
  private val prisonerSearchApiClient: PrisonerSearchApiClient,
  private val manageAdjudicationsApiClient: ManageAdjudicationsApiClient,
) {
  fun report() {
  }

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
    const val CHUNK_SIZE = 100
  }
}
