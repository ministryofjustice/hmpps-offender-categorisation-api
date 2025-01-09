package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.integration

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.test.context.jdbc.Sql

@DisplayName("/released-prisoners-with-active-categorisations")
class ReportReleasedPrisonersWithActiveCategorisationsResourceTest : IntegrationTestBase() {
  @Nested
  @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
  inner class Report {
    @Test
    fun `correctly returns 200 response`() {
      webTestClient.get().uri("/released-prisoners-with-active-categorisations/report")
        .exchange()
        .expectStatus().isOk
    }
  }
}
