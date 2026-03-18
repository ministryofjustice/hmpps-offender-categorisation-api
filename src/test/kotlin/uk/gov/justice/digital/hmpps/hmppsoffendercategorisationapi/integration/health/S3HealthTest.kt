package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.integration.health

import org.junit.jupiter.api.Test
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.integration.IntegrationTestBase

@AutoConfigureWebTestClient
class S3HealthTest : IntegrationTestBase() {

  @Test
  fun `S3 Health check returns OK`() {
    webTestClient.get()
      .uri("/health")
      .exchange()
      .expectStatus()
      .isOk()
      .expectBody()
      .jsonPath("status").isEqualTo("UP")
      .jsonPath("components.s3Health.status").isEqualTo("UP")
      .jsonPath("components.s3Health.details.bucket").isEqualTo(s3Properties.bucketName)
      .jsonPath("components.s3Health.details.region").isEqualTo(s3Properties.region)
      .returnResult()
  }
}
