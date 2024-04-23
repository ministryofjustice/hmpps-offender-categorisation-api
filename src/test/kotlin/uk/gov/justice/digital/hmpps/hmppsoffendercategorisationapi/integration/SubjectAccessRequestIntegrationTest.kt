package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.integration

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.test.context.jdbc.Sql

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
    inner class HappyPath {
      @Sql("classpath:repository/subject_access_request_service_data.sql")
      @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
      @Test
      fun `should return data if prisoner exists`() {
        webTestClient.get().uri("/subject-access-request?prn=GXXXX")
          .headers(setHeaders(roles = listOf("ROLE_SAR_DATA_ACCESS")))
          .exchange()
          .expectStatus().isOk
          .expectBody()
          .consumeWith(System.out::println)
          .jsonPath("$.content.categorisationTool.catForm.offender_no").isEqualTo("GXXXX")
      }

      @Sql("classpath:repository/subject_access_request_service_data.sql")
      @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
      @Test
      fun `should omit data if none exists`() {
        webTestClient.get().uri("/subject-access-request?prn=GBBBB")
          .headers(setHeaders(roles = listOf("ROLE_SAR_DATA_ACCESS")))
          .exchange()
          .expectStatus().isOk
          .expectBody()
          .consumeWith(System.out::println)
          .jsonPath("$.content.categorisationTool").hasJsonPath()
          .jsonPath("$.content.categorisationTool").isEmpty()
      }
    }
  }
}
