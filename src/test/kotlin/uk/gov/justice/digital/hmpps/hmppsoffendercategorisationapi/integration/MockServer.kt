package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.http.HttpHeader
import com.github.tomakehurst.wiremock.http.HttpHeaders
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.TestPrisonFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.TestPrisonerFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.Prison
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.Prisoner

private const val MAPPINGS_DIRECTORY = "src/testIntegration/resources"

open class MockServer(port: Int) :
  WireMockServer(
    WireMockConfiguration.wireMockConfig()
      .port(port)
      .usingFilesUnderDirectory(MAPPINGS_DIRECTORY),
  )

class PrisonerSearchMockServer : MockServer(8091) {
  fun stubFindPrisonerByPrisonerNumber(prisoner: Prisoner = (TestPrisonerFactory()).build()) {
    stubFor(
      WireMock.post(WireMock.urlEqualTo("/prisoner-search/prisoner-numbers"))
        .withRequestBody(WireMock.equalToJson("{\"prisonerNumbers\":  [\"123ABC\"]}"))
        .willReturn(
          WireMock.aResponse()
            .withHeaders(HttpHeaders(HttpHeader("Content-Type", "application/json")))
            .withBody(
              jacksonObjectMapper().apply {
                registerModule(JavaTimeModule())
              }.writeValueAsString(listOf(prisoner)),
            ),
        ),
    )
  }
}

class PrisonApiMockServer : MockServer(8094) {
  fun stubFindPrisons(prisons: List<Prison> = listOf((TestPrisonFactory()).build())) {
    stubFor(
      WireMock.get(WireMock.urlEqualTo("/api/agencies/prisons"))
        .willReturn(
          WireMock.aResponse()
            .withHeaders(HttpHeaders(HttpHeader("Content-Type", "application/json")))
            .withBody(
              jacksonObjectMapper().apply {
                registerModule(JavaTimeModule())
              }.writeValueAsString(prisons),
            ),
        ),
    )
  }
}

class ManageAdjudicationsMockServer : MockServer(8092) {
  fun stubFindAdjudicationsByBookingId(bookingId: Int, numberOfAdjudications: Int?) {
    stubFor(
      WireMock.get(WireMock.urlEqualTo("/adjudications/by-booking-id/$bookingId"))
        .willReturn(
          WireMock.aResponse()
            .withHeaders(HttpHeaders(HttpHeader("Content-Type", "application/json")))
            .withBody(
              jacksonObjectMapper().apply {
                registerModule(JavaTimeModule())
              }.writeValueAsString(
                mapOf(
                  "bookingId" to bookingId,
                  "adjudicationCount" to numberOfAdjudications,
                ),
              ),
            ),
        ),
    )
  }
}

class ManageOffencesMockServer : MockServer(8093) {
  fun stubCheckWhichOffenceCodesAreSdsExcluded(offenceCodes: List<String>) {
    stubFor(
      WireMock.get(WireMock.urlPathEqualTo("/schedule/sds-early-release-exclusions"))
        .willReturn(
          WireMock.aResponse()
            .withHeaders(HttpHeaders(HttpHeader("Content-Type", "application/json")))
            .withBody(
              jacksonObjectMapper().apply {
                registerModule(JavaTimeModule())
              }.writeValueAsString(
                if (offenceCodes.isEmpty()) {
                  listOf()
                } else {
                  listOf(
                    mapOf(
                      "offenceCode" to offenceCodes[0],
                      "schedulePart" to "violence",
                    ),
                  )
                },
              ),
            ),
        ),
    )
  }
}

class HmppsAuthMockServer : MockServer(8090) {
  private val mapper = ObjectMapper()

  fun stubGrantToken() {
    stubFor(
      WireMock.post(WireMock.urlEqualTo("/auth/oauth/token"))
        .willReturn(
          WireMock.aResponse()
            .withHeaders(HttpHeaders(HttpHeader("Content-Type", "application/json")))
            .withBody(mapper.writeValueAsString(mapOf("access_token" to "ABCDE", "token_type" to "bearer"))),
        ),
    )
  }
}
