package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.data.domain.PageRequest
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonerSearchApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.TestFormEntityFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.TestPrisonerFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.FormEntity
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.Prisoner
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.FormRepository

@ExtendWith(OutputCaptureExtension::class)
@ExtendWith(MockitoExtension::class)
class ReleasedPrisonersWithActiveCategorisationServiceTest() {
  private val mockFormRepository = mock<FormRepository>()
  private val mockPrisonerSearchApiClient = mock<PrisonerSearchApiClient>()

  private val releasedPrisonersWithActiveCategorisationService = ReleasedPrisonersWithActiveCategorisationService(
    mockFormRepository,
    mockPrisonerSearchApiClient,
  )

  @Test
  fun testReport(output: CapturedOutput) {
    val testOffenderId1 = "123ABC"
    val testOffenderId2 = "456DEF"
    whenever(
      mockFormRepository.findAllByStatusNotIn(
        listOf(FormEntity.STATUS_APPROVED, FormEntity.STATUS_CANCELLED),
        PageRequest.of(
          0,
          ReleasedPrisonersWithActiveCategorisationService.CHUNK_SIZE,
        ),
      ),
    ).thenReturn(
      listOf<FormEntity>(
        (TestFormEntityFactory()).withStatus(FormEntity.STATUS_STARTED).withOffenderNo(testOffenderId1).build(),
        (TestFormEntityFactory()).withStatus(FormEntity.STATUS_STARTED).withOffenderNo(testOffenderId2).withCatType(FormEntity.CAT_TYPE_INITIAL).build(),
      ),
    )

    whenever(mockPrisonerSearchApiClient.findPrisonersByPrisonerNumbers(listOf<String>(testOffenderId1, testOffenderId2))).thenReturn(
      listOf<Prisoner>(
        (TestPrisonerFactory()).withPrisonerNumber(testOffenderId1).withStatus(Prisoner.STATUS_ACTIVE_IN).build(),
        (TestPrisonerFactory()).withPrisonerNumber(testOffenderId2).withStatus(Prisoner.STATUS_INACTIVE_OUT).build(),
      ),
    )

    releasedPrisonersWithActiveCategorisationService.report()

    assertThat(output).contains("Prisoner $testOffenderId2 has active categorisation of type ${FormEntity.CAT_TYPE_INITIAL} but prisoner search shows them to have status ${Prisoner.STATUS_INACTIVE_OUT} and restricted patient value false")
    assert(true)
  }
}
