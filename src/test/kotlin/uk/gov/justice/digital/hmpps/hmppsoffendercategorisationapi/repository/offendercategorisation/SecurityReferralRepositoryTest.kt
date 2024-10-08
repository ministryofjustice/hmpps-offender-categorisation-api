package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.config.ResourceTest

class SecurityReferralRepositoryTest : ResourceTest() {
  @Autowired
  lateinit var repository: SecurityReferralRepository

  @Test
  @Sql("classpath:repository/security_referral.sql")
  @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = AFTER_TEST_METHOD)
  fun `Should Find by Offender No`() {
    val securityReferral = repository.findByOffenderNoOrderByRaisedDateDesc("G2550VO")

    assertThat(securityReferral?.first()?.userId).isEqualTo("LBENNETT_GEN")
  }
}
