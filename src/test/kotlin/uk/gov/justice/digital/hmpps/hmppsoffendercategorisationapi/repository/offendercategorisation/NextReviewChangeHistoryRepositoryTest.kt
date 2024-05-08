package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.config.ResourceTest

class NextReviewChangeHistoryRepositoryTest : ResourceTest() {
  @Autowired
  lateinit var repository: NextReviewChangeHistoryRepository

  @Test
  @Sql("classpath:repository/next_review_change_history.sql")
  @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = AFTER_TEST_METHOD)
  fun `Should Find by Offender No`() {
    val nextReviewChangeHistory = repository.findByOffenderNo("G7919UD")

    assertThat(nextReviewChangeHistory?.reason).isEqualTo("testing")
  }
}
