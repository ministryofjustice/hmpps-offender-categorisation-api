package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.config.RepositoryTest
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.*


class TransformersTest : RepositoryTest() {
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

  @Test
  @Sql("classpath:repository/risk_change.sql")
  @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  fun `Should transform risk change data to response`() {

    val entity = riskChangeRepository.findByOffenderNo("G0048VL")

    val riskChange = transform(entity)
    Assertions.assertThat(riskChange.status).isEqualTo("LBENNETT_GEN")
  }

  @Test
  @Sql("classpath:repository/security_referral.sql")
  @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  fun `Should transform security referral data to response`() {

    val entity = securityReferralRepository.findByOffenderNo("G0048VL")

    val securityReferral = transform(entity)
    Assertions.assertThat(securityReferral.status).isEqualTo("LBENNETT_GEN")
  }

  @Test
  @Sql("classpath:repository/next_review_change_history.sql")
  @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  fun `Should transform next review change history data to response`() {

    val entity = nextReviewChangeHistoryRepository.findByOffenderNo("G0048VL")

    val nextReviewChangeHistory = transform(entity)
    Assertions.assertThat(nextReviewChangeHistory.nextReviewDate).isEqualTo("LBENNETT_GEN")
  }

  @Test
  @Sql("classpath:repository/lite_category.sql")
  @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  fun `Should transform liste category data to response`() {

    val entity = liteCategoryRepository.findByOffenderNo("G0048VL")

    val liteCategory = transform(entity)
    Assertions.assertThat(liteCategory.nextReviewDate).isEqualTo("LBENNETT_GEN")
  }

  @Test
  @Sql("classpath:repository/form.sql")
  @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  fun `Should transform form data to response`() {

    val entity = formRepository.findByOffenderNo("G0048VL")

    val form = transform(entity)
    Assertions.assertThat(form.formResponse).isEqualTo("LBENNETT_GEN")
  }
}