package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
class HmppsOffenderCategorisationApi

fun main(args: Array<String>) {
  runApplication<HmppsOffenderCategorisationApi>(*args)
}

@Configuration
@EnableScheduling
class SchedulingConfiguration
