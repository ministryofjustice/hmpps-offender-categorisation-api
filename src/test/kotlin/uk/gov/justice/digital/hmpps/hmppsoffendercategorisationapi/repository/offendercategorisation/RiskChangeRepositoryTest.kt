package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.config.ResourceTest

class RiskChangeRepositoryTest : ResourceTest() {
  @Autowired
  lateinit var repository: RiskChangeRepository

  @Test
  @Sql("classpath:repository/risk_change.sql")
  @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = AFTER_TEST_METHOD)
  fun `Should Find First by Offender No Order By Raised Date`() {
    val riskChange = repository.findFirstByOffenderNoOrderByRaisedDateDesc("G0048VL")

    assertThat(riskChange?.prisonId).isEqualTo("3")
  }
}
