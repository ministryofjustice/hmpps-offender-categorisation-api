package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.integration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient
import org.springframework.http.MediaType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.jdbc.Sql
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.prisonerAlert.PrisonerAlertResponseDto
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.TestPrisonFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.TestPrisonerFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.PrisonerRiskProfileEntity
import java.time.ZoneId
import java.time.ZonedDateTime

class PrisonerRiskPollerIntegrationTest : IntegrationTestBase() {

  @Autowired
  private lateinit var jdbcTemplate: JdbcTemplate

  @Nested
  @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
  @Sql(scripts = ["classpath:repository/prisonerRiskProfile.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
  @AutoConfigureWebTestClient
  inner class PollPrisonersRisk {
    @Test
    fun `successfully polls prisoner risk and updates record`() {
      val testPrisonId = "LEI"
      prisonApiMockServer.stubFindPrisons(listOf(TestPrisonFactory().withAgencyId(testPrisonId).build()))
      prisonerSearchMockServer.stubFindPrisonersByPrisonId(testPrisonId, listOf(TestPrisonerFactory().withPrisonerNumber("ABC123").build()))
      prisonerAlertsApiMockServer.stubFindPrisonerAlerts(
        "ABC123",
        listOf(
          PrisonerAlertResponseDto.ALERT_CODE_ESCAPE_RISK,
          PrisonerAlertResponseDto.ALERT_CODE_ESCAPE_LIST,
          PrisonerAlertResponseDto.ALERT_CODE_ESCAPE_LIST_HEIGHTENED,
          PrisonerAlertResponseDto.ALERT_CODE_OCGM,
        ),
        mutableListOf<PrisonerAlertResponseDto>(),
      )
      prisonApiMockServer.stubGetAssaultIncidents("ABC123", emptyList())
      webTestClient.get().uri("/prisoner-risk-calculation/poll-prisons")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk

      val result = jdbcTemplate.query(
        "SELECT * FROM public.prisoner_risk_profile",
      ) { rs, _ ->
        PrisonerRiskProfileEntity(
          offenderNo = rs.getString("offender_no"),
          riskProfile = rs.getString("risk_profile"),
          calculatedAt = ZonedDateTime.ofInstant(rs.getTimestamp("calculated_at").toInstant(), ZoneId.systemDefault()),
        )
      }

      assertThat(result.count()).isEqualTo(1)
      assertThat(result[0].riskProfile).isEqualTo("{\"escapeListAlerts\": [], \"escapeRiskAlerts\": [], \"riskDueToViolence\": false, \"riskDueToSeriousOrganisedCrime\": false}")
    }
  }
}
