package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.config.ResourceTest
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.BaseSarUnitTest
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.FormRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.LiteCategoryRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.NextReviewChangeHistoryRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.RiskChangeRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.SecurityReferralRepository

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

  val json = jacksonObjectMapper()

  @Test
  @Sql("classpath:repository/risk_change.sql")
  @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  fun `Should transform risk change data to response`() {
    val riskChange = transform(riskChangeRepository.findByOffenderNo("G0048VL"))

    val expectedResult = BaseSarUnitTest.loadExpectedOutput("/transformer/risk_change.json")
    Assertions.assertThat(json.writeValueAsString(riskChange)).isEqualTo(expectedResult)
  }

  @Test
  @Sql("classpath:repository/security_referral.sql")
  @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  fun `Should transform security referral data to response`() {
    val securityReferral = transform(securityReferralRepository.findByOffenderNo("G2550VO"))

    val expectedResult = BaseSarUnitTest.loadExpectedOutput("/transformer/security_referral.json")

    Assertions.assertThat(json.writeValueAsString(securityReferral)).isEqualTo(expectedResult)
  }

  @Test
  @Sql("classpath:repository/next_review_change_history.sql")
  @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  fun `Should transform next review change history data to response`() {
    val nextReviewChangeHistory = transform(nextReviewChangeHistoryRepository.findByOffenderNo("G7919UD"))

    val expectedResult = BaseSarUnitTest.loadExpectedOutput("/transformer/next_review_change_history.json")

    Assertions.assertThat(json.writeValueAsString(nextReviewChangeHistory)).isEqualTo(expectedResult)
  }

  @Test
  @Sql("classpath:repository/lite_category.sql")
  @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  fun `Should transform list category data to response`() {
    val liteCategory = transform(liteCategoryRepository.findByOffenderNo("G0089UO"))

    val expectedResult = BaseSarUnitTest.loadExpectedOutput("/transformer/lite_category.json")

    Assertions.assertThat(json.writeValueAsString(liteCategory)).isEqualTo(expectedResult)
  }

  @Test
  @Sql("classpath:repository/form.sql")
  @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  fun `Should transform form data to response`() {
    val form = transform(formRepository.findByOffenderNo("G8105VR"))

    val expectedResult = BaseSarUnitTest.loadExpectedOutput("/transformer/form.json")

    Assertions.assertThat(json.writeValueAsString(form)).isEqualTo(expectedResult)
  }
}
