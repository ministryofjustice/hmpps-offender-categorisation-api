package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.argThat
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.TestFormEntityFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.FormEntity
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.FormRepository

@ExtendWith(MockitoExtension::class)
class FormServiceTest {
  val mockFormRepository = Mockito.mock<FormRepository>()

  val formService = FormService(mockFormRepository)

  val testBookingId = 12345L
  val testUserId = "TEST_GEN"
  val testSecurityReview = "Test security review"

  @Test
  fun testSaveSecurityReview() {
    whenever(mockFormRepository.findByBookingId(testBookingId)).thenReturn(
      TestFormEntityFactory()
        .withFormResponse("{}")
        .withBookingId(testBookingId)
        .withStatus(FormEntity.STATUS_STARTED)
        .withApprovalDate(null)
        .build(),
    )
    formService.saveSecurityReview(testBookingId, testUserId, true, testSecurityReview)

    verify(mockFormRepository, times(1)).findByBookingId(testBookingId)
    verify(mockFormRepository, times(1)).save(
      argThat { entity ->
        entity.bookingId == testBookingId &&
          entity.getStatus() == FormEntity.STATUS_SECURITY_BACK &&
          entity.getSecurityReviewedBy() == testUserId
        entity.getFormResponse()!!.contains("{security={review={securityReview=Test security review}}}")
      },
    )
    assert(true)
  }

  @Test
  fun testSaveSecurityReviewWithoutSubmitting() {
    whenever(mockFormRepository.findByBookingId(testBookingId)).thenReturn(
      TestFormEntityFactory()
        .withFormResponse("{}")
        .withBookingId(testBookingId)
        .withStatus(FormEntity.STATUS_STARTED)
        .withApprovalDate(null)
        .build(),
    )
    formService.saveSecurityReview(testBookingId, testUserId, false, testSecurityReview)

    verify(mockFormRepository, times(1)).findByBookingId(testBookingId)
    verify(mockFormRepository, times(1)).save(
      argThat { entity ->
        entity.bookingId == testBookingId &&
          entity.getStatus() == FormEntity.STATUS_STARTED &&
          entity.getSecurityReviewedBy() == null
        entity.getFormResponse()!!.contains("{security={review={securityReview=Test security review}}}")
      },
    )
    assert(true)
  }
}
