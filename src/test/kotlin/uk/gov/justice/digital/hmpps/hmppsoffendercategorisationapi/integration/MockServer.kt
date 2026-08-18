package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.http.HttpHeader
import com.github.tomakehurst.wiremock.http.HttpHeaders
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PageableResult
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.SearchResult
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentReport
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentReportResponse
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.ReportBasic
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.prisonerAlert.PrisonerAlertResponseDto
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.TestPrisonFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.TestPrisonerFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.RestPage
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.enum.RiskLevel
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.enum.SdsExemptionSchedulePart
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.Prison
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.Prisoner
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.adjudication.Adjudication
import java.util.UUID
import java.util.stream.Collectors

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

  fun stubFindPrisoner(prisoner: Prisoner = (TestPrisonerFactory()).build()) {
    stubFor(
      WireMock.get(WireMock.urlEqualTo("/prisoner/${prisoner.prisonerNumber}"))
        .willReturn(
          WireMock.aResponse()
            .withHeaders(HttpHeaders(HttpHeader("Content-Type", "application/json")))
            .withBody(
              jacksonObjectMapper().apply {
                registerModule(JavaTimeModule())
              }.writeValueAsString(prisoner),
            ),
        ),
    )
  }

  fun stubFindPrisonersByPrisonId(prisonId: String, prisoners: List<Prisoner> = listOf((TestPrisonerFactory()).build())) {
    stubFor(
      WireMock.get(WireMock.urlEqualTo("/prisoner-search/prison/$prisonId?page=0&size=100"))
        .willReturn(
          WireMock.aResponse()
            .withHeaders(HttpHeaders(HttpHeader("Content-Type", "application/json")))
            .withBody(
              jacksonObjectMapper().apply {
                registerModule(JavaTimeModule())
              }.writeValueAsString(
                SearchResult(prisoners),
              ),
            ),
        ),
    )
  }
}

class IncidentApiMockServer : MockServer(8098) {

  fun stubCountAssaultIncidents(numberOfIncidents: Long) {
    stubFor(
      WireMock.get(WireMock.urlPathEqualTo("/incident-reports"))
        .willReturn(
          WireMock.aResponse()
            .withHeaders(HttpHeaders(HttpHeader("Content-Type", "application/json")))
            .withBody(
              jacksonObjectMapper().apply {
                registerModule(JavaTimeModule())
              }.writeValueAsString(
                IncidentReportResponse(
                  totalElements = numberOfIncidents,
                  content = listOf(),
                ),
              ),
            ),
        ),
    )
  }

  fun stubSearchAssaultIncidents(assaultIncidentIds: List<UUID>) {
    stubFor(
      WireMock.get(WireMock.urlPathEqualTo("/incident-reports"))
        .willReturn(
          WireMock.aResponse()
            .withHeaders(HttpHeaders(HttpHeader("Content-Type", "application/json")))
            .withBody(
              jacksonObjectMapper().apply {
                registerModule(JavaTimeModule())
              }.writeValueAsString(
                IncidentReportResponse(
                  totalElements = assaultIncidentIds.size.toLong(),
                  content = assaultIncidentIds.map { id ->
                    ReportBasic(id = id)
                  },
                ),
              ),
            ),
        ),
    )
  }

  fun stubGetAssaultIncidentById(id: UUID, incidentReport: IncidentReport) {
    stubFor(
      WireMock.get(WireMock.urlPathEqualTo("/incident-reports/$id/with-details"))
        .willReturn(
          WireMock.aResponse()
            .withHeaders(HttpHeaders(HttpHeader("Content-Type", "application/json")))
            .withBody(
              jacksonObjectMapper().apply {
                registerModule(JavaTimeModule())
              }.writeValueAsString(incidentReport),
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
  fun stubFindAdjudicationsByAgencyId(result: List<Adjudication>) {
    stubFor(
      WireMock.get(WireMock.urlPathEqualTo("/reported-adjudications/reports"))
        .willReturn(
          WireMock.aResponse()
            .withHeaders(HttpHeaders(HttpHeader("Content-Type", "application/json")))
            .withBody(
              jacksonObjectMapper().apply {
                registerModule(JavaTimeModule())
              }.writeValueAsString(
                PageableResult(
                  content = result,
                ),
              ),
            ),
        ),
    )
  }
}

class AssessRisksAndNeedsMockServer : MockServer(8095) {
  fun stubFindRiskSummary(crn: String, overallRiskLevel: RiskLevel = RiskLevel.LOW) {
    stubFor(
      WireMock.get(WireMock.urlEqualTo("/risks/crn/$crn/summary"))
        .willReturn(
          WireMock.aResponse()
            .withHeaders(HttpHeaders(HttpHeader("Content-Type", "application/json")))
            .withBody(
              jacksonObjectMapper().apply {
                registerModule(JavaTimeModule())
              }.writeValueAsString(
                mapOf(
                  "overallRiskLevel" to overallRiskLevel,
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
                      "schedulePart" to SdsExemptionSchedulePart.VIOLENT,
                    ),
                  )
                },
              ),
            ),
        ),
    )
  }
}

class PrisonerAlertsMockServer : MockServer(8097) {
  fun stubFindPrisonerAlerts(prisonerNumber: String, alertCodes: List<String>, alerts: MutableList<PrisonerAlertResponseDto>) {
    val commaSeparatedAlertCodes = alertCodes.stream().collect(Collectors.joining(","))
    stubFor(
      WireMock.get(WireMock.urlEqualTo("/prisoners/$prisonerNumber/alerts?alertCode=$commaSeparatedAlertCodes"))
        .willReturn(
          WireMock.aResponse()
            .withHeaders(HttpHeaders(HttpHeader("Content-Type", "application/json")))
            .withBody(
              jacksonObjectMapper().apply {
                registerModule(JavaTimeModule())
              }.writeValueAsString(
                RestPage<PrisonerAlertResponseDto>(
                  content = alerts,
                  size = 100,
                  page = 0,
                  total = alerts.size.toLong(),
                ),
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
