package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.config.ResourceTest

class LiteCategoryRepositoryTest : ResourceTest() {
  @Autowired
  lateinit var repository: LiteCategoryRepository

  @Test
  @Sql("classpath:repository/lite_category.sql")
  @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  fun `Should Find by Offender No`() {
    val lightCategoryRecords = repository.findByOffenderNoAndCreatedDateBetweenOrApprovedDateBetweenOrderBySequenceDesc("G0089UO")

    Assertions.assertThat(lightCategoryRecords?.first()?.assessedBy).isEqualTo("LBENNETT_GEN")
  }
}
