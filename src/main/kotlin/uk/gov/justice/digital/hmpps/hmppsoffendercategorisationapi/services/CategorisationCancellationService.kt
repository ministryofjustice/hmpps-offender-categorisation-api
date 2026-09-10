package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services

import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.FormEntity
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.FormRepository
import java.time.Clock
import java.time.ZonedDateTime

@Service
class CategorisationCancellationService(
  private val formRepository: FormRepository,
  private val clock: Clock,
  private val prisonApiClient: PrisonApiClient,
) {
  @Transactional
  fun cancelCategorisation(formEntity: FormEntity, deleteFormResponse: Boolean) {
    formEntity.setStatus(FormEntity.STATUS_CANCELLED_AFTER_RELEASE)
    formEntity.setCancelledDate(ZonedDateTime.now(clock).toLocalDateTime())
    if (deleteFormResponse) {
      formEntity.setFormResponse("{}")
    }
    formRepository.save(formEntity)
    log.info("Categorisation cancelled for ${formEntity.bookingId}")
    prisonApiClient.setPendingCategorisationsInactive(formEntity.bookingId)
  }

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}
