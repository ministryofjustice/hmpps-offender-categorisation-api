package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import org.springframework.boot.info.BuildProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

const val ROLE_HDC_ADMIN = "HDC_ADMIN"

@Configuration
class OpenApiConfiguration(
  buildProperties: BuildProperties,
) {
  private val version: String = buildProperties.version

  @Bean
  fun customOpenAPI(): OpenAPI = OpenAPI()
    .info(
      Info().title("HMPPS Offender Categorisation Api Documentation")
        .version(version)
        .description("API for Offender Categorisation")
        .contact(Contact().name("HMPPS Digital Studio").email("feedback@digital.justice.gov.uk")),
    )
}
