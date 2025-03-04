package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.prs

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.AssessRisksAndNeedsApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.ManageAdjudicationsApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.ManageOffencesApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonerSearchApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.ProbationSearchApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.TestPrisonFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.TestPrisonerFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.enum.RiskLevel
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.enum.SdsExemptionSchedulePart
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.Prisoner
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.RiskSummary
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.SdsExcludedOffenceCode
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.adjudication.AdjudicationsSummary
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.prisoner.Alert
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.prisoner.ConvictedOffence
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.prisoner.CurrentIncentive
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.prisoner.Level
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.probation.ProbationOtherIds
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.probation.ProbationPerson
import java.time.LocalDate

@ExtendWith(OutputCaptureExtension::class)
@ExtendWith(MockitoExtension::class)
class PrsEligibilityServiceTest {
  private val mockPrisonerSearchApiClient = Mockito.mock<PrisonerSearchApiClient>()
  private val mockPrisonApiClient = Mockito.mock<PrisonApiClient>()
  private val mockManageOffencesApiClient = Mockito.mock<ManageOffencesApiClient>()
  private val mockProbationSearchApiClient = Mockito.mock<ProbationSearchApiClient>()
  private val mockAssessRisksAndNeedsApiClient = Mockito.mock<AssessRisksAndNeedsApiClient>()
  private val mockManageAdjudicationsApiClient = Mockito.mock<ManageAdjudicationsApiClient>()

  private val prsEligibilityService = PrsEligibilityService(
    mockPrisonerSearchApiClient,
    mockPrisonApiClient,
    mockManageOffencesApiClient,
    mockProbationSearchApiClient,
    mockAssessRisksAndNeedsApiClient,
    mockManageAdjudicationsApiClient,
  )

  @Test
  fun testReport(output: CapturedOutput) {
    val testAgencyId = "HCI"
    val testPrisonerNumber = "ABC123"
    val testPrisonerNumber2 = "DEF456"
    val testCrn = "CRN_TEST"
    val testOffenceCode = "SOMETHING"
    val testOffenceCode2 = "SOMETHING_ELSE"
    val testBookingId = 12
    val testBookingId2 = 13

    whenever(mockPrisonApiClient.findPrisons()).thenReturn(
      listOf(
        (TestPrisonFactory()).withAgencyId(testAgencyId).build(),
      ),
    )
    whenever(mockPrisonerSearchApiClient.findPrisonersByAgencyId(testAgencyId, 0, 100)).thenReturn(
      listOf(
        (TestPrisonerFactory())
          .withPrisonerNumber(testPrisonerNumber)
          .withBookingId(testBookingId)
          .withCategory(Prisoner.CATEGORY_C)
          .withCurrentIncentive(CurrentIncentive(Level(Prisoner.INCENTIVE_LEVEL_STANDARD, "Standard")))
          .withAlerts(null)
          .withReleaseDate(LocalDate.now().plusMonths(6))
          .withConvictedOffencesResponse(
            listOf(
              ConvictedOffence(
                offenceCode = testOffenceCode,
                offenceDescription = "something",
              ),
            ),
          )
          .build(),
        (TestPrisonerFactory())
          .withPrisonerNumber(testPrisonerNumber2)
          .withBookingId(testBookingId2)
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
            listOf(
              ConvictedOffence(
                offenceCode = testOffenceCode,
                offenceDescription = "something",
              ),
              ConvictedOffence(
                offenceCode = testOffenceCode2,
                offenceDescription = "something else",
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
          schedulePart = SdsExemptionSchedulePart.NONE,
        ),
        SdsExcludedOffenceCode(
          offenceCode = testOffenceCode2,
          schedulePart = SdsExemptionSchedulePart.SEXUAL,
        ),
      ),
    )
    whenever(mockProbationSearchApiClient.findProbationPersonsFromPrisonNumbers(listOf(testPrisonerNumber, testPrisonerNumber2))).thenReturn(
      listOf(
        ProbationPerson(
          otherIds = ProbationOtherIds(
            crn = testCrn,
            nomsNumber = testPrisonerNumber2,
          ),
        ),
      ),
    )
    whenever(mockAssessRisksAndNeedsApiClient.findRiskSummary(testCrn)).thenReturn(RiskSummary(RiskLevel.HIGH))
    whenever(mockManageAdjudicationsApiClient.findAdjudicationsByBookingId(testBookingId)).thenReturn(
      AdjudicationsSummary(testBookingId, 0),
    )
    whenever(mockManageAdjudicationsApiClient.findAdjudicationsByBookingId(testBookingId2)).thenReturn(
      AdjudicationsSummary(testBookingId, 4),
    )

    prsEligibilityService.report()

    Assertions.assertThat(output).contains("PRS_ELIGIBILITY_INVESTIGATION: HCI, 2, 1, UnknownRosh: 1, CATEGORY: 1, TIME_LEFT_TO_SERVE: 1, INCENTIVE_LEVEL: 1, ESCAPE: 1, OFFENCE_SEXUAL: 1, HIGH_ROSH: 1, ADJUDICATION: 1")
  }
}
