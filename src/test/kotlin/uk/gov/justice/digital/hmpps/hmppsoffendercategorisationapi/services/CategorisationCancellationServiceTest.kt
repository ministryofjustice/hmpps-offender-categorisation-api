package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services

import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.argThat
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.TestFormEntityFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.FormEntity
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.FormRepository
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class CategorisationCancellationServiceTest {
  val mockFormRepository: FormRepository = Mockito.mock(FormRepository::class.java)
  private val frozenDateTime = "2025-01-01T10:40:34Z"
  val fixedClock: Clock = Clock.fixed(Instant.parse(frozenDateTime), ZoneId.of("UTC"))
  val mockPrisonApiClient: PrisonApiClient = Mockito.mock(PrisonApiClient::class.java)
  val categorisationCancellationService = CategorisationCancellationService(mockFormRepository, fixedClock, mockPrisonApiClient)

  @Test
  fun testCancelAnyInProgressReviewsDueToPrisonerRelease() {
    val testBookingId = 5L
    val testFormResponse = "{\"something\": \"something\"}"
    val testFormEntity = TestFormEntityFactory()
      .withFormResponse(testFormResponse)
      .withStatus(FormEntity.STATUS_STARTED)
      .withBookingId(testBookingId)
      .build()
    categorisationCancellationService.cancelCategorisationAfterRelease(testFormEntity, true)

    verify(mockFormRepository, times(1)).save(
      argThat { entity ->
        entity.getStatus() == FormEntity.STATUS_CANCELLED_AFTER_RELEASE &&
          entity.getCancelledDate() == LocalDateTime.ofInstant(Instant.parse(frozenDateTime), ZoneId.of("UTC")) &&
          entity.getFormResponse() == "{}"
      },
    )
    verify(mockPrisonApiClient, times(1)).setPendingCategorisationsInactive(testBookingId)
  }
}
