package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HmppsOffenderCategorisationApi

fun main(args: Array<String>) {
  runApplication<HmppsOffenderCategorisationApi>(*args)
}
