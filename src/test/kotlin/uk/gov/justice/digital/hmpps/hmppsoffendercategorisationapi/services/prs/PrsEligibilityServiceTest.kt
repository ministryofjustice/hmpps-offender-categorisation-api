package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.prs

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonerSearchApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.TestPrisonFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.TestPrisonerFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.Prisoner
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.prisoner.Alert
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.prisoner.Alerts
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.prisoner.CurrentIncentive
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.prisoner.Level
import java.time.LocalDate

@ExtendWith(OutputCaptureExtension::class)
@ExtendWith(MockitoExtension::class)
class PrsEligibilityServiceTest {
  private val mockPrisonerSearchApiClient = Mockito.mock<PrisonerSearchApiClient>()
  private val mockPrisonApiClient = Mockito.mock<PrisonApiClient>()

  private val prsEligibilityService = PrsEligibilityService(
    mockPrisonerSearchApiClient,
    mockPrisonApiClient,
  )

  @Test
  fun testReport(output: CapturedOutput) {
    val testAgencyId = "HCI"

    whenever(mockPrisonApiClient.findPrisons()).thenReturn(
      listOf(
        (TestPrisonFactory()).withAgencyId(testAgencyId).build(),
      ),
    )
    whenever(mockPrisonerSearchApiClient.findPrisonersByAgencyId(testAgencyId, 0, 100)).thenReturn(
      listOf(
        (TestPrisonerFactory())
          .withCategory(Prisoner.CATEGORY_C)
          .withCurrentIncentive(CurrentIncentive(Level(Prisoner.INCENTIVE_LEVEL_STANDARD, "Standard")))
          .withAlerts(null)
          .withReleaseDate(LocalDate.now().plusMonths(6))
          .build(),
        (TestPrisonerFactory())
          .withCategory(Prisoner.CATEGORY_B)
          .withCurrentIncentive(CurrentIncentive(Level(Prisoner.INCENTIVE_LEVEL_BASIC, "Basic")))
          .withAlerts(
            Alerts(
              listOf(
                Alert(
                  Alert.ESCAPE_RISK_ALERT_CODE,
                  true,
                  false,
                ),
              ),
            ),
          )
          .withReleaseDate(LocalDate.now().plusMonths(13))
          .build(),
      ),
    )

    prsEligibilityService.report()

    Assertions.assertThat(output).contains("PRS_ELIGIBILITY_INVESTIGATION: HCI, 2, 1, 1, 1, 1, 1")
    assert(true)
  }
}
