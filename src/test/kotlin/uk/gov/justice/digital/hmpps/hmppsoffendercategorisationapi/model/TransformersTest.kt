package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
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

class TransformersTest : ResourceTest() {
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

  @Test
  @Sql("classpath:repository/risk_change.sql")
  @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  fun `Should transform risk change data to response`() {
    val riskChange = transformRiskChange(riskChangeRepository.findByOffenderNoOrderByRaisedDateDesc("G0048VL"))

    val expectedResult = BaseSarUnitTest.loadExpectedOutput("/transformer/risk_change.json")
    Assertions.assertThat(json.writeValueAsString(riskChange)).isEqualTo(expectedResult)
  }

  @Test
  @Sql("classpath:repository/security_referral.sql")
  @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  fun `Should transform security referral data to response`() {
    val securityReferral = transformSecurityReferral(securityReferralRepository.findByOffenderNoOrderByRaisedDateDesc("G2550VO"))

    val expectedResult = BaseSarUnitTest.loadExpectedOutput("/transformer/security_referral.json")

    Assertions.assertThat(json.writeValueAsString(securityReferral)).isEqualTo(expectedResult)
  }

  @Test
  @Sql("classpath:repository/next_review_change_history.sql")
  @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  fun `Should transform next review change history data to response`() {
    val nextReviewChangeHistory =
      transformNextReviewChangeHistory(nextReviewChangeHistoryRepository.findByOffenderNo("G7919UD"))

    val expectedResult = BaseSarUnitTest.loadExpectedOutput("/transformer/next_review_change_history.json")

    Assertions.assertThat(json.writeValueAsString(nextReviewChangeHistory)).isEqualTo(expectedResult)
  }

  @Test
  @Sql("classpath:repository/lite_category.sql")
  @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  fun `Should transform list category data to response`() {
    val liteCategory = transformLiteCategory(liteCategoryRepository.findByOffenderNoOrderBySequenceDesc("G0089UO"))

    val expectedResult = BaseSarUnitTest.loadExpectedOutput("/transformer/lite_category.json")

    Assertions.assertThat(json.writeValueAsString(liteCategory)).isEqualTo(expectedResult)
  }

  @Test
  @Sql("classpath:repository/form.sql")
  @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  fun `Should transform form data to response`() {
    val form = transformAllFromCatForm(formRepository.findAllByOffenderNoOrderBySequenceNoAsc("G8105VR"))

    val expectedResult = BaseSarUnitTest.loadExpectedOutput("/transformer/form.json")

    Assertions.assertThat(json.writeValueAsString(form)).isEqualTo(listOf(expectedResult))
  }

  @Test
  @Sql("classpath:repository/risk_profiler.sql")
  @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  fun `Should transform risk profiler data to response`() {
    val riskProfiler = transform(previousProfileRepository.findByOffenderNo("G8105VR"))

    val expectedResult = BaseSarUnitTest.loadExpectedOutput("/transformer/risk_profiler.json")

    Assertions.assertThat(json.writeValueAsString(riskProfiler)).isEqualTo(expectedResult)
  }
}
