package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.config.RepositoryTest

class LiteCategoryRepositoryTest : RepositoryTest() {
  @Autowired
  lateinit var repository: LiteCategoryRepository

  @Test
  @Sql("classpath:repository/lite_category.sql")
  @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  fun `Should Find by Offender No`() {

    val securityReferral = repository.findByOffenderNo("G0089UO")

    Assertions.assertThat(securityReferral.assessedBy).isEqualTo("LBENNETT_GEN")
  }
}