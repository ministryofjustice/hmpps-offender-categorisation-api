package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.config.ResourceTest
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.BaseSarUnitTest
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.SarResponse
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.FormRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.LiteCategoryRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.NextReviewChangeHistoryRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.RiskChangeRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.SecurityReferralRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.riskprofiler.PreviousProfileRepository
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

  @Autowired
  lateinit var previousProfileRepository: PreviousProfileRepository

  val json = jacksonObjectMapper()

  private val liteCategoryRepositoryMock = mock<LiteCategoryRepository>()
  private val riskChangeRepositoryMock = mock<RiskChangeRepository>()
  private val securityReferralRepositoryMock = mock<SecurityReferralRepository>()
  private val formRepositoryMock = mock<FormRepository>()
  private val previousProfileRepositoryMock = mock<PreviousProfileRepository>()
  private val nextReviewChangeHistoryRepositoryMock = mock<NextReviewChangeHistoryRepository>()

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
      previousProfileRepository,
    )

    val response = subjectAccessRequestService.getPrisonContentFor("GXXXX", now(), now())
    println(json.writeValueAsString(response?.content as SarResponse))
    assertThat(json.writerWithDefaultPrettyPrinter().writeValueAsString(response.content as SarResponse)).isEqualTo(
      BaseSarUnitTest.loadExpectedOutput("/subject_access_request_content_v2.json"),
    )
  }

  @Test
  fun `should build empty response if offender not found in tables`() {
    val subjectAccessRequestService = SubjectAccessRequestService(
      securityReferralRepository,
      nextReviewChangeHistoryRepository,
      riskChangeRepository,
      liteCategoryRepository,
      formRepository,
      previousProfileRepository,
    )

    val response = subjectAccessRequestService.getPrisonContentFor("GNOTFOUND", now(), now())

    assertThat(response).isEqualTo(null)
  }

  @Test
  fun `should build response with empty data`() {
    whenever(liteCategoryRepositoryMock.findByOffenderNoOrderBySequenceDesc(OFFENDER_NO)).thenReturn(null)
    whenever(formRepositoryMock.findAllByOffenderNoAndStartDateBetweenOrApprovalDateBetweenOrderBySequenceNoAsc(OFFENDER_NO)).thenReturn(
      listOf(),
    )
    whenever(riskChangeRepositoryMock.findByOffenderNoOrderByRaisedDateDesc(OFFENDER_NO)).thenReturn(null)
    whenever(nextReviewChangeHistoryRepositoryMock.findByOffenderNo(OFFENDER_NO)).thenReturn(null)
    whenever(securityReferralRepositoryMock.findByOffenderNoOrderByRaisedDateDesc(OFFENDER_NO)).thenReturn(null)
    whenever(previousProfileRepositoryMock.findByOffenderNo(OFFENDER_NO)).thenReturn(null)

    val subjectAccessRequestService = SubjectAccessRequestService(
      securityReferralRepositoryMock,
      nextReviewChangeHistoryRepositoryMock,
      riskChangeRepositoryMock,
      liteCategoryRepositoryMock,
      formRepositoryMock,
      previousProfileRepositoryMock,
    )

    val response = subjectAccessRequestService.getPrisonContentFor("GRED", now(), now())
    assertThat(response).isEqualTo(null)
  }

  private companion object {
    const val OFFENDER_NO = "G143"
  }
}
