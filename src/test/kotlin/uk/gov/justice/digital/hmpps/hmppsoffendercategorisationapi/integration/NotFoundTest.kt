package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.integration

import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppshdcapi.config.ROLE_HDC_ADMIN

class NotFoundTest : IntegrationTestBase() {

  @Test
  fun `Resources that aren't found should return 404 - test of the exception handler`() {
    webTestClient.get().uri("/some-url-not-found")
      .headers(setAuthorisation(roles = listOf("ROLE_$ROLE_HDC_ADMIN")))
      .exchange()
      .expectStatus().isNotFound
  }
}
