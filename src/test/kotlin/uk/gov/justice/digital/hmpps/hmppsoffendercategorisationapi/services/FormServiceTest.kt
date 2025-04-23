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
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@ExtendWith(MockitoExtension::class)
class FormServiceTest {
  val mockFormRepository = Mockito.mock<FormRepository>()
  private val frozenDateTime = "2025-01-01T10:40:34Z"
  val fixedClock = Clock.fixed(Instant.parse(frozenDateTime), ZoneId.of("UTC"))

  val formService = FormService(mockFormRepository, fixedClock)

  val testBookingId = 12345L
  val testUserId = "TEST_GEN"
  val testSecurityReview = "Test security review"

  @Test
  fun testSaveSecurityReview() {
    whenever(mockFormRepository.findFirstByBookingIdAndStatusNotOrderBySequenceNoDesc(testBookingId)).thenReturn(
      TestFormEntityFactory()
        .withFormResponse("{}")
        .withBookingId(testBookingId)
        .withStatus(FormEntity.STATUS_STARTED)
        .withApprovalDate(null)
        .build(),
    )
    formService.saveSecurityReview(testBookingId, testUserId, true, testSecurityReview)

    verify(mockFormRepository, times(1)).findFirstByBookingIdAndStatusNotOrderBySequenceNoDesc(testBookingId)
    verify(mockFormRepository, times(1)).save(
      argThat { entity ->
        entity.bookingId == testBookingId &&
          entity.getStatus() == FormEntity.STATUS_SECURITY_BACK &&
          entity.getSecurityReviewedBy() == testUserId
        entity.getFormResponse()!!.contains("{\"security\":{\"review\":{\"securityReview\":\"Test security review\"}}}")
      },
    )
    assert(true)
  }

  @Test
  fun testSaveSecurityReviewWithoutSubmitting() {
    whenever(mockFormRepository.findFirstByBookingIdAndStatusNotOrderBySequenceNoDesc(testBookingId)).thenReturn(
      TestFormEntityFactory()
        .withFormResponse("{}")
        .withBookingId(testBookingId)
        .withStatus(FormEntity.STATUS_STARTED)
        .withApprovalDate(null)
        .build(),
    )
    formService.saveSecurityReview(testBookingId, testUserId, false, testSecurityReview)

    verify(mockFormRepository, times(1)).findFirstByBookingIdAndStatusNotOrderBySequenceNoDesc(testBookingId)
    verify(mockFormRepository, times(1)).save(
      argThat { entity ->
        entity.bookingId == testBookingId &&
          entity.getStatus() == FormEntity.STATUS_STARTED &&
          entity.getSecurityReviewedBy() == null
        entity.getFormResponse()!!.contains("{\"security\":{\"review\":{\"securityReview\":\"Test security review\"}}}")
      },
    )
  }

  @Test
  fun testCancelAnyInProgressReviewsDueToPrisonerRelease() {
    val testOffenderNo = "ABC123"
    val testFormResponse = "{\"something\": \"something\"}"
    whenever(mockFormRepository.findAllByOffenderNoAndStatusNotIn(testOffenderNo, listOf(FormEntity.STATUS_APPROVED, FormEntity.STATUS_CANCELLED, FormEntity.STATUS_CANCELLED_AFTER_RELEASE)))
      .thenReturn(
        listOf(
          TestFormEntityFactory()
            .withFormResponse(testFormResponse)
            .withStatus(FormEntity.STATUS_STARTED)
            .build(),
        ),
      )
    formService.cancelAnyInProgressReviewsDueToPrisonerRelease(testOffenderNo)

    verify(mockFormRepository, times(1)).findAllByOffenderNoAndStatusNotIn(testOffenderNo, listOf(FormEntity.STATUS_APPROVED, FormEntity.STATUS_CANCELLED, FormEntity.STATUS_CANCELLED_AFTER_RELEASE))
    verify(mockFormRepository, times(1)).save(
      argThat { entity ->
        entity.getStatus() == FormEntity.STATUS_CANCELLED_AFTER_RELEASE &&
          entity.getCancelledDate() == LocalDateTime.ofInstant(Instant.parse(frozenDateTime), ZoneId.of("UTC")) &&
          entity.getFormResponse() == "{}"
      },
    )
  }
}
