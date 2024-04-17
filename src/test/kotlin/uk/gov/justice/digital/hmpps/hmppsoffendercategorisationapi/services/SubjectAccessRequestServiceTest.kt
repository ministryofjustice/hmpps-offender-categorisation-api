package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.config.ResourceTest
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.BaseSarUnitTest
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.SarResponse
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.FormRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.LiteCategoryRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.NextReviewChangeHistoryRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.RiskChangeRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.SecurityReferralRepository
import java.time.LocalDate.now

class SubjectAccessRequestServiceTest : ResourceTest() {

  @Autowired
  lateinit var riskChangeRepository: RiskChangeRepository

  @Autowired
  lateinit var securityReferralRepository: SecurityReferralRepository

  @Autowired
  lateinit var liteCategoryRepository: LiteCategoryRepository

  @Autowired
  lateinit var nextReviewChangeHistoryRepository: NextReviewChangeHistoryRepository

  @Autowired
  lateinit var formRepository: FormRepository

  private val liteCategoryRepositoryMock = mock<LiteCategoryRepository>()
  private val riskChangeRepositoryMock = mock<RiskChangeRepository>()
  private val securityReferralRepositoryMock = mock<SecurityReferralRepository>()
  private val formRepositoryMock = mock<FormRepository>()
  private val nextReviewChangeHistoryRepositoryMock = mock<NextReviewChangeHistoryRepository>()

  private val json = Json { prettyPrint = true }

  @Test
  @Sql("classpath:repository/subject_access_request_service_data.sql")
  @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  fun `should build response to match response defined in acceptance criteria`() {
    val subjectAccessRequestService = SubjectAccessRequestService(
      securityReferralRepository,
      nextReviewChangeHistoryRepository,
      riskChangeRepository,
      liteCategoryRepository,
      formRepository,
    )

    val response = subjectAccessRequestService.getPrisonContentFor("GXXXX", now(), now())

    assertThat(json.encodeToString(response?.content as SarResponse)).isEqualTo(BaseSarUnitTest.loadExpectedOutput("/subject_access_request_content.text"))
  }

  @Test
  fun `should build empty response if offender not found in tables`() {
    val subjectAccessRequestService = SubjectAccessRequestService(
      securityReferralRepository,
      nextReviewChangeHistoryRepository,
      riskChangeRepository,
      liteCategoryRepository,
      formRepository,
    )

    val response = subjectAccessRequestService.getPrisonContentFor("GNOTFOUND", now(), now())

    assertThat(Json.encodeToString(response?.content as SarResponse)).isEqualTo("{\"categorisationTool\":{}}")
  }

  @Test
  fun `should build response with empty data`() {
    whenever(liteCategoryRepositoryMock.findByOffenderNo(OFFENDER_NO)).thenReturn(null)
    whenever(formRepositoryMock.findByOffenderNo(OFFENDER_NO)).thenReturn(null)
    whenever(riskChangeRepositoryMock.findByOffenderNo(OFFENDER_NO)).thenReturn(null)
    whenever(nextReviewChangeHistoryRepositoryMock.findByOffenderNo(OFFENDER_NO)).thenReturn(null)
    whenever(securityReferralRepositoryMock.findByOffenderNo(OFFENDER_NO)).thenReturn(null)

    val subjectAccessRequestService = SubjectAccessRequestService(
      securityReferralRepositoryMock,
      nextReviewChangeHistoryRepositoryMock,
      riskChangeRepositoryMock,
      liteCategoryRepositoryMock,
      formRepositoryMock,
    )

    val response = subjectAccessRequestService.getPrisonContentFor("GRED", now(), now())
    assertThat(Json.encodeToString(response?.content as SarResponse)).isEqualTo("{\"categorisationTool\":{}}")
    println(Json.encodeToString(response.content as SarResponse))
  }

  private companion object {
    const val OFFENDER_NO = "G143"
  }
}
