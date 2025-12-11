package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.FormEntity
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.FormRepository
import java.time.Clock
import java.time.ZonedDateTime

@Service
class FormService(
  private val formRepository: FormRepository,
  private val prisonApiClient: PrisonApiClient,
  private val clock: Clock,
) {
  fun saveSecurityReview(bookingId: Long, userId: String, submitted: Boolean, securityReview: String?) {
    val formEntity = formRepository.findFirstByBookingIdAndStatusNotOrderBySequenceNoDesc(bookingId)
      ?: throw FormNotFoundException(bookingId)
    if (securityReview != null) {
      formEntity.updateFormResponse(
        FormEntity.FORM_RESPONSE_SECTION_SECURITY,
        FormEntity.FORM_RESPONSE_FIELD_NAME,
        mapOf(Pair("securityReview", securityReview)),
      )
    }
    if (submitted) {
      formEntity.setStatus(FormEntity.STATUS_SECURITY_BACK)
      formEntity.setSecurityReviewedBy(userId)
      formEntity.setSecurityReviewedDate(ZonedDateTime.now().toLocalDateTime())
    }
    formRepository.save(formEntity)
  }

  fun cancelAnyInProgressReviewsDueToPrisonerRelease(offenderNo: String, deleteFormResponse: Boolean = true) {
    val formEntities = formRepository.findAllByOffenderNoAndStatusNotIn(
      offenderNo,
      listOf(FormEntity.STATUS_APPROVED, FormEntity.STATUS_CANCELLED, FormEntity.STATUS_CANCELLED_AFTER_RELEASE),
    )
    formEntities.forEach {
      log.error("${it.bookingId} HERE")
      val cancelledTime = ZonedDateTime.now(clock).toLocalDateTime()
      if (it.nomisSequenceNo != null) {
        log.error("Rejecting pending categorisation for bookingId ${it.bookingId} and sequence ${it.nomisSequenceNo} due to prisoner release")
        try {
          prisonApiClient.rejectPendingCategorisations(
            bookingId = it.bookingId,
            sequenceNumber = it.nomisSequenceNo,
            evaluationDate = cancelledTime.toLocalDate(),
            reviewCommitteeCode = CANCELLATION_REVIEW_COMMITTEE_CODE,
          )
        } catch (ex: Exception) {
          log.error(
            "Failed to reject pending categorisation for bookingId ${it.bookingId} and sequence ${it.sequenceNo}",
            ex,
          )
        }
      }
      it.setStatus(FormEntity.STATUS_CANCELLED_AFTER_RELEASE)
      it.setCancelledDate(cancelledTime)
      if (deleteFormResponse) {
        it.setFormResponse("{}")
      }
      formRepository.save(it)
    }
  }

  companion object {
    const val CANCELLATION_REVIEW_COMMITTEE_CODE = "SECUR"

    private val log = LoggerFactory.getLogger(this::class.java)
  }
}

class FormNotFoundException(bookingId: Long) : Exception("Unable to find form record for $bookingId")
