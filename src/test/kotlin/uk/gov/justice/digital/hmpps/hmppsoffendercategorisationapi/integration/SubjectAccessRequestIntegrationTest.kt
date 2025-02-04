package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.integration

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.test.context.jdbc.Sql
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.BaseSarUnitTest

class SubjectAccessRequestIntegrationTest : IntegrationTestBase() {
  @Nested
  @DisplayName("/subject-access-request")
  inner class SubjectAccessRequestEndpoint {

    @Nested
    inner class Security {
      @Test
      fun `access forbidden when no authority`() {
        webTestClient.get().uri("/subject-access-request?prn=A12345")
          .exchange()
          .expectStatus().isUnauthorized
      }

      // @Test
      fun `access forbidden when no role`() {
        webTestClient.get().uri("/subject-access-request?prn=A12345")
          .headers(setHeaders(roles = listOf()))
          .exchange()
          .expectStatus().isForbidden
      }

      // @Test
      fun `access forbidden with wrong role`() {
        webTestClient.get().uri("/subject-access-request?prn=A12345")
          .headers(setHeaders(roles = listOf("ROLE_BANANAS")))
          .exchange()
          .expectStatus().isForbidden
      }
    }

    @Nested
    @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
    @Sql(scripts = ["classpath:repository/subject_access_request_service_data.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
    inner class HappyPath {
      @Test
      fun `should return data if prisoner exists`() {
        webTestClient.get().uri("/subject-access-request?prn=GXXXX")
          .headers(setHeaders(roles = listOf("ROLE_SAR_DATA_ACCESS")))
          .exchange()
          .expectStatus().isOk
          .expectBody()
          .consumeWith(System.out::println)
          .json("{\"content\":${BaseSarUnitTest.loadExpectedOutput("/subject_access_request_content_v2.json")}}")
      }

      @Test
      fun `should omit data if none exists`() {
        webTestClient.get().uri("/subject-access-request?prn=GBBBB")
          .headers(setHeaders(roles = listOf("ROLE_SAR_DATA_ACCESS")))
          .exchange()
          .expectStatus().isNoContent
      }
    }

    @Nested
    @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
    @Sql(scripts = ["classpath:repository/subject_access_request_service_date_test_data.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
    inner class DateTest {
      @Test
      fun `should return data between date range`() {
        webTestClient.get().uri("/subject-access-request?prn=123ABC&fromDate=2020-01-01&toDate=2021-01-01")
          .headers(setHeaders(roles = listOf("ROLE_SAR_DATA_ACCESS")))
          .exchange()
          .expectStatus().isOk
          .expectBody()
          .consumeWith(System.out::println)
          .json(BaseSarUnitTest.loadExpectedOutput("/subject_access_reqest_content_for_date_test.json"))
      }
    }
  }
}
