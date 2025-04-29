package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client

import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.WebClientResponseException

class ClientUtility {
  companion object {
    fun isNotFoundError(e: Throwable?) = e is WebClientResponseException && e.statusCode == HttpStatus.NOT_FOUND
  }
}
