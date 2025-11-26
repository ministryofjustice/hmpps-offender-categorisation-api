package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.risk

import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.argThat
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonerSearchApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.TestPrisonFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.TestPrisonerFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.risk.TestPrisonerRiskProfileFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.PrisonerRiskProfileRepository
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

class PrisonerRiskPollerTest {
  private val mockPrisonApiClient = mock<PrisonApiClient>()
  private val mockPrisonerSearchApiClient = mock<PrisonerSearchApiClient>()
  private val mockPrisonerRiskCalculator = mock<PrisonerRiskCalculator>()
  private val mockPrisonerRiskProfileRepository = mock<PrisonerRiskProfileRepository>()
  private val frozenDateTime = "2025-01-01T10:40:34Z"
  private val fixedClock = Clock.fixed(Instant.parse(frozenDateTime), ZoneId.of("UTC"))

  private val prisonerRiskPoller = PrisonerRiskPoller(
    mockPrisonApiClient,
    mockPrisonerSearchApiClient,
    mockPrisonerRiskCalculator,
    mockPrisonerRiskProfileRepository,
    fixedClock,
  )

  @Test
  fun `should poll prisoners risk`() {
    whenever(mockPrisonApiClient.findPrisons()).thenReturn(
      listOf(
        TestPrisonFactory().withAgencyId("TEST").build(),
      ),
    )
    whenever(mockPrisonerSearchApiClient.findPrisonersByAgencyId("TEST", 0, 100)).thenReturn(
      listOf(TestPrisonerFactory().withPrisonerNumber("A1234BC").build()),
    )
    whenever(mockPrisonerRiskCalculator.calculateRisk("A1234BC")).thenReturn(
      TestPrisonerRiskProfileFactory().build(),
    )
    prisonerRiskPoller.pollPrisonersRisk()
    verify(mockPrisonerRiskProfileRepository, times(1)).save(
      argThat { entity ->
        entity.offenderNo == "A1234BC" &&
          entity.riskProfile == """{"escapeRiskAlerts":[],"escapeListAlerts":[],"riskDueToSeriousOrganisedCrime":false,"riskDueToViolence":false}""" &&
          entity.calculatedAt == Instant.parse(frozenDateTime).atZone(ZoneId.of("UTC"))
      },
    )
  }
}
