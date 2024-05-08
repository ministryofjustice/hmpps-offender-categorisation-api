package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.riskprofiler

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.config.ResourceTest
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.SecurityReferralRepository

class PreviousProfileRepositoryTest : ResourceTest() {
  @Autowired
  lateinit var repository: PreviousProfileRepository

  @Test
  @Sql("classpath:repository/previous_profile.sql")
  @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = AFTER_TEST_METHOD)
  fun `Should Find by Offender No`() {
    val entity = repository.findByOffenderNo("GXXXX")

    assertThat(entity?.offenderNo).isEqualTo("GXXXX")
  }
}
