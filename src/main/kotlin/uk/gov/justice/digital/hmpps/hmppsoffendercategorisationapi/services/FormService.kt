package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.FormEntity
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.FormRepository
import java.time.Clock
import java.time.ZonedDateTime

@Service
class FormService(
  private val formRepository: FormRepository,
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

  fun cancelAnyInProgressReviewsDueToPrisonerRelease(offenderNo: String) {
    val formEntities = formRepository.findAllByOffenderNoAndStatusNotIn(
      offenderNo,
      listOf(FormEntity.STATUS_APPROVED, FormEntity.STATUS_CANCELLED),
    )
    formEntities.forEach {
      it.setStatus(FormEntity.STATUS_CANCELLED_AFTER_RELEASE)
      it.setCancelledDate(ZonedDateTime.now(clock).toLocalDateTime())
      formRepository.save(it)
    }
  }
}

class FormNotFoundException(bookingId: Long) : Exception("Unable to find form record for $bookingId")
