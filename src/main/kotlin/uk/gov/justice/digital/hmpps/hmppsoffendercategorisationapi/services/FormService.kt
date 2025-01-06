package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.FormEntity
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.FormRepository
import java.time.ZonedDateTime

@Service
class FormService(
  private val formRepository: FormRepository,
) {
  fun saveSecurityReview(bookingId: Long, userId: String, submitted: Boolean, securityReview: String?) {
    val formEntity = formRepository.findByBookingId(bookingId)
      ?: throw Exception("Could not find form entity for booking ID $bookingId")
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
    log.info(formEntity.getFormResponse())
    formRepository.save(formEntity)
  }

  private companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}
