package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.config.ResourceTest
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.FormRepository

class FormRepositoryTest : ResourceTest() {
  @Autowired
  lateinit var repository: FormRepository

  @Test
  @Sql("classpath:repository/form.sql")
  @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  fun `Should Find by Offender No`() {
    val securityReferral = repository.findByOffenderNo("G8105VR")

    Assertions.assertThat(securityReferral?.cancelledBy).isEqualTo("SRENDELL_GEN")
  }
}
