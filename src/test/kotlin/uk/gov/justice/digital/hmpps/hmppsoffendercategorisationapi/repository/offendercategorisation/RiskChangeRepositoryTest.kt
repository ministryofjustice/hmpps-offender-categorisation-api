package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.InvalidDataAccessResourceUsageException
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.config.ResourceTest
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.RiskChangeEntity
import java.time.ZonedDateTime

class RiskChangeRepositoryTest : ResourceTest() {
  @Autowired
  lateinit var repository: RiskChangeRepository

  @Test
  @Sql("classpath:repository/risk_change.sql")
  @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = AFTER_TEST_METHOD)
  fun `Should Find by Offender No`() {
    val riskChange = repository.findByOffenderNoOrderByRaisedDateDesc("G0048VL")

    assertThat(riskChange.first().userId).isEqualTo("LBENNETT_GEN")
  }

  @Test
  fun `should throw invalid data access when saving string into jsonb columns`() {
    assertThatThrownBy {
      repository.save(
        RiskChangeEntity(
          oldProfile = """{"escapeRiskAlerts":[],"escapeListAlerts":[],"riskDueToViolence":false,"riskDueToSeriousOrganisedCrime":false}""",
          newProfile = """{"escapeRiskAlerts":[],"escapeListAlerts":[],"riskDueToViolence":true,"riskDueToSeriousOrganisedCrime":false}""",
          offenderNo = "A1234BC",
          prisonId = "TEST",
          status = RiskChangeEntity.STATUS_NEW,
          raisedDate = ZonedDateTime.now(),
        ),
      )
    }
      .isInstanceOf(InvalidDataAccessResourceUsageException::class.java)
      .hasMessageContaining("column \"new_profile\" is of type jsonb but expression is of type character varying")
  }
}
