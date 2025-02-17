package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.prs

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.ManageOffencesApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonerSearchApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.TestPrisonFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.TestPrisonerFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.Prisoner
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.SdsExcludedOffenceCode
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.prisoner.*
import java.time.LocalDate

@ExtendWith(OutputCaptureExtension::class)
@ExtendWith(MockitoExtension::class)
class PrsEligibilityServiceTest {
  private val mockPrisonerSearchApiClient = Mockito.mock<PrisonerSearchApiClient>()
  private val mockPrisonApiClient = Mockito.mock<PrisonApiClient>()
  private val mockManageOffencesApiClient = Mockito.mock<ManageOffencesApiClient>()

  private val prsEligibilityService = PrsEligibilityService(
    mockPrisonerSearchApiClient,
    mockPrisonApiClient,
    mockManageOffencesApiClient,
  )

  @Test
  fun testReport(output: CapturedOutput) {
    val testAgencyId = "HCI"
    val testOffenceCode = "SOMETHING"
    val testOffenceCode2 = "SOMETHING_ELSE"

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
          .withConvictedOffencesResponse(
            ConvictedOffencesResponse(
              allConvictedOffences = listOf(
                ConvictedOffence(
                  offenceCode = testOffenceCode,
                  offenceDescription = "something",
                ),
                ConvictedOffence(
                  offenceCode = testOffenceCode2,
                  offenceDescription = "something else",
                ),
              ),
            ),
          )
          .build(),
        (TestPrisonerFactory())
          .withCategory(Prisoner.CATEGORY_B)
          .withCurrentIncentive(CurrentIncentive(Level(Prisoner.INCENTIVE_LEVEL_BASIC, "Basic")))
          .withAlerts(
            listOf(
              Alert(
                Alert.ESCAPE_RISK_ALERT_CODE,
                true,
                false,
              ),
            ),
          )
          .withConvictedOffencesResponse(
            ConvictedOffencesResponse(
              allConvictedOffences = listOf(
                ConvictedOffence(
                  offenceCode = testOffenceCode,
                  offenceDescription = "something",
                ),
              ),
            ),
          )
          .withReleaseDate(LocalDate.now().plusMonths(13))
          .build(),
      ),
    )
    whenever(mockManageOffencesApiClient.checkWhichOffenceCodesAreSdsExcluded(listOf(testOffenceCode, testOffenceCode2))).thenReturn(
      listOf(
        SdsExcludedOffenceCode(
          offenceCode = testOffenceCode,
          schedulePart = "something",
        ),
      ),
    )

    prsEligibilityService.report()

    Assertions.assertThat(output).contains("PRS_ELIGIBILITY_INVESTIGATION: HCI, 2, 1, 0, CATEGORY: 1, TIME_LEFT_TO_SERVE: 1, INCENTIVE_LEVEL: 1, ESCAPE: 1, SOMETHING: 2")
  }
}
