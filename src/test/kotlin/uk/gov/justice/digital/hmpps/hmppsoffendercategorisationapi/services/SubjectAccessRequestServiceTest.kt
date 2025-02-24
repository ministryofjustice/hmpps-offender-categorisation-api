package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.config.ResourceTest
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.BaseSarUnitTest
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.FormRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.LiteCategoryRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.NextReviewChangeHistoryRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.RiskChangeRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.SecurityReferralRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.riskprofiler.PreviousProfileRepository

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
    val testPrisonerNumber = "GXXXX"

    val subjectAccessRequestService = SubjectAccessRequestService(
      securityReferralRepository,
      nextReviewChangeHistoryRepository,
      riskChangeRepository,
      liteCategoryRepository,
      formRepository,
      previousProfileRepository,
    )

    val response = subjectAccessRequestService.getPrisonContentFor(testPrisonerNumber, null, null)
    println(json.writeValueAsString(response?.content))
    val expectedResult = BaseSarUnitTest.loadExpectedOutput("/subject_access_request_content_2.json").let { json.readValue<Any>(it) }
    val jsonResponse = json.writeValueAsString(response?.content).let { json.readValue<Any>(it) }
    assertThat(jsonResponse).isEqualTo(
      expectedResult,
    )
  }

  @Test
  fun `should build empty response if offender not found in tables`() {
    val testPrisonerNumber = "GNOTFOUND"
    val subjectAccessRequestService = SubjectAccessRequestService(
      securityReferralRepository,
      nextReviewChangeHistoryRepository,
      riskChangeRepository,
      liteCategoryRepository,
      formRepository,
      previousProfileRepository,
    )

    val response = subjectAccessRequestService.getPrisonContentFor(testPrisonerNumber, null, null)

    assertThat(response).isEqualTo(null)
  }

  @Test
  fun `should build response with empty data`() {
    whenever(liteCategoryRepositoryMock.findAllByOffenderNoOrderBySequenceDesc(OFFENDER_NO)).thenReturn(null)
    whenever(formRepositoryMock.findAllByOffenderNoOrderBySequenceNoAsc(OFFENDER_NO)).thenReturn(
      listOf(),
    )
    whenever(riskChangeRepositoryMock.findByOffenderNoOrderByRaisedDateDesc(OFFENDER_NO)).thenReturn(null)
    whenever(nextReviewChangeHistoryRepositoryMock.findByOffenderNo(OFFENDER_NO)).thenReturn(null)
    whenever(securityReferralRepositoryMock.findByOffenderNoOrderByRaisedDateDesc(OFFENDER_NO)).thenReturn(null)
    whenever(previousProfileRepositoryMock.findByOffenderNo(OFFENDER_NO)).thenReturn(null)
    val testPrisonerNumber = "GRED"

    val subjectAccessRequestService = SubjectAccessRequestService(
      securityReferralRepositoryMock,
      nextReviewChangeHistoryRepositoryMock,
      riskChangeRepositoryMock,
      liteCategoryRepositoryMock,
      formRepositoryMock,
      previousProfileRepositoryMock,
    )

    val response = subjectAccessRequestService.getPrisonContentFor(testPrisonerNumber, null, null)
    assertThat(response).isEqualTo(null)
  }

  private companion object {
    const val OFFENDER_NO = "G143"
  }
}
