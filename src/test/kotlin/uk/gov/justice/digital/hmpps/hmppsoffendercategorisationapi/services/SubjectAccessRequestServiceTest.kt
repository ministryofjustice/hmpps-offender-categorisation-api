package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.LiteCategoryEntity
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.BaseSarUnitTest
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.SarResponse
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.*
import java.time.LocalDate.now

class SubjectAccessRequestServiceTest: BaseSarUnitTest() {

  private val liteCategoryRepository = mock<LiteCategoryRepository>()
  private val riskChangeRepository = mock<RiskChangeRepository>()
  private val securityReferralRepository = mock<SecurityReferralRepository>()
  private val formRepository = mock<FormRepository>()
  private val nextReviewChangeHistoryRepository = mock<NextReviewChangeHistoryRepository>()

  private val subjectAccessRequestService = SubjectAccessRequestService(
    securityReferralRepository,
    nextReviewChangeHistoryRepository,
    riskChangeRepository,
    liteCategoryRepository,
    formRepository,
  )

  @Test
  fun `should build response to match response defined in acceptance criteria`() {

    whenever(liteCategoryRepository.findByOffenderNo(OFFENDER_NO)).thenReturn(LITE_CATEGORY)
    whenever(formRepository.findByOffenderNo(OFFENDER_NO)).thenReturn(catForm)
    whenever(riskChangeRepository.findByOffenderNo(OFFENDER_NO)).thenReturn(riskChange)
    whenever(nextReviewChangeHistoryRepository.findByOffenderNo(OFFENDER_NO)).thenReturn(nextReviewChangeHistory)
    whenever(securityReferralRepository.findByOffenderNo(OFFENDER_NO)).thenReturn(securityReferral)

    val response = subjectAccessRequestService.getPrisonContentFor("GRED")

  }

  @Test
  fun `should build response with empty data`() {

    whenever(liteCategoryRepository.findByOffenderNo(OFFENDER_NO)).thenReturn(null)
    whenever(formRepository.findByOffenderNo(OFFENDER_NO)).thenReturn(null)
    whenever(riskChangeRepository.findByOffenderNo(OFFENDER_NO)).thenReturn(null)
    whenever(nextReviewChangeHistoryRepository.findByOffenderNo(OFFENDER_NO)).thenReturn(null)
    whenever(securityReferralRepository.findByOffenderNo(OFFENDER_NO)).thenReturn(null)

    val response = subjectAccessRequestService.getPrisonContentFor("GRED", now(), now())
    assertThat(Json.encodeToString(response?.content as SarResponse)).isEqualTo("{\"categorisationTool\":{}}")
    println(Json.encodeToString(response?.content as SarResponse))
  }

  private companion object {
    const val OFFENDER_NO = "G143"
  }
}
