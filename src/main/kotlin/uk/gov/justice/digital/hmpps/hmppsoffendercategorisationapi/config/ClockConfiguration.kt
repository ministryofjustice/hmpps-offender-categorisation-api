package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

@Configuration
@Profile("test")
class ClockConfiguration {
  @Bean
  fun clock(): Clock = Clock.fixed(TIMESTAMP.toInstant(), ZoneId.systemDefault())

  companion object {
    val TIMESTAMP = ZonedDateTime.of(LocalDateTime.of(2022, 1, 2, 3, 4, 5), ZoneId.systemDefault())
  }
}
