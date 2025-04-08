package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.time.Clock

@Configuration
class OffenderCategorisationApiConfiguration {
  @Bean
  @Profile("!test")
  fun clock(): Clock? = Clock.systemDefaultZone()
}
