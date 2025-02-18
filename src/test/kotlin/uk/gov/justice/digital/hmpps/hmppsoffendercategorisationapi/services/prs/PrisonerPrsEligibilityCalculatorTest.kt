package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.prs

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.TestPrisonerFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.enum.PrsIneligibilityReason
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.Prisoner
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.prisoner.Alert
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.prisoner.ConvictedOffence
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.prisoner.CurrentIncentive
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.prisoner.Level
import java.time.LocalDate

class PrisonerPrsEligibilityCalculatorTest {

  @Test
  fun testCalculateWithEligiblePrisoner() {
    val testOffenceCode = "SOMETHING"
    val prisonerPrsEligibilityCalculator = PrisonerPrsEligibilityCalculator(
      (TestPrisonerFactory())
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
      listOf(testOffenceCode),
    )
    val prisonerPrsEligibility = prisonerPrsEligibilityCalculator.calculate()
    Assertions.assertThat(prisonerPrsEligibility.reasonForIneligibility).isEmpty()
    Assertions.assertThat(prisonerPrsEligibility.isEligible).isTrue()
    Assertions.assertThat(prisonerPrsEligibility.sdsExcludedOffenceCodes).contains(testOffenceCode)
  }

  @Test
  fun testCalculateWithCategoryB() {
    val prisonerPrsEligibilityCalculator = PrisonerPrsEligibilityCalculator(
      (TestPrisonerFactory())
        .withCategory(Prisoner.CATEGORY_B)
        .withCurrentIncentive(CurrentIncentive(Level(Prisoner.INCENTIVE_LEVEL_STANDARD, "Standard")))
        .withAlerts(null)
        .withReleaseDate(LocalDate.now().plusMonths(6))
        .build(),
      emptyList(),
    )
    val prisonerPrsEligibility = prisonerPrsEligibilityCalculator.calculate()
    Assertions.assertThat(prisonerPrsEligibility.reasonForIneligibility).contains(PrsIneligibilityReason.CATEGORY)
    Assertions.assertThat(prisonerPrsEligibility.isEligible).isFalse()
  }

  @Test
  fun testCalculateWithBasicIncentiveLevel() {
    val prisonerPrsEligibilityCalculator = PrisonerPrsEligibilityCalculator(
      (TestPrisonerFactory())
        .withCategory(Prisoner.CATEGORY_C)
        .withCurrentIncentive(CurrentIncentive(Level(Prisoner.INCENTIVE_LEVEL_BASIC, "Basic")))
        .withAlerts(null)
        .withReleaseDate(LocalDate.now().plusMonths(6))
        .build(),
      emptyList(),
    )
    val prisonerPrsEligibility = prisonerPrsEligibilityCalculator.calculate()
    Assertions.assertThat(prisonerPrsEligibility.reasonForIneligibility).contains(PrsIneligibilityReason.INCENTIVE_LEVEL)
    Assertions.assertThat(prisonerPrsEligibility.isEligible).isFalse()
  }

  @Test
  fun testCalculateWith13MonthsToServe() {
    val prisonerPrsEligibilityCalculator = PrisonerPrsEligibilityCalculator(
      (TestPrisonerFactory())
        .withCategory(Prisoner.CATEGORY_C)
        .withCurrentIncentive(CurrentIncentive(Level(Prisoner.INCENTIVE_LEVEL_STANDARD, "Standard")))
        .withAlerts(null)
        .withReleaseDate(LocalDate.now().plusMonths(13))
        .build(),
      emptyList(),
    )
    val prisonerPrsEligibility = prisonerPrsEligibilityCalculator.calculate()
    Assertions.assertThat(prisonerPrsEligibility.reasonForIneligibility).contains(PrsIneligibilityReason.TIME_LEFT_TO_SERVE)
    Assertions.assertThat(prisonerPrsEligibility.isEligible).isFalse()
  }

  @Test
  fun testCalculateWithEscapeAlert() {
    val prisonerPrsEligibilityCalculator = PrisonerPrsEligibilityCalculator(
      (TestPrisonerFactory())
        .withCategory(Prisoner.CATEGORY_C)
        .withCurrentIncentive(CurrentIncentive(Level(Prisoner.INCENTIVE_LEVEL_STANDARD, "Standard")))
        .withAlerts(
          listOf(
            Alert(
              Alert.ESCAPE_RISK_ALERT_CODE,
              true,
              false,
            ),
          ),
        )
        .withReleaseDate(LocalDate.now().plusMonths(6))
        .build(),
      emptyList(),
    )
    val prisonerPrsEligibility = prisonerPrsEligibilityCalculator.calculate()
    Assertions.assertThat(prisonerPrsEligibility.reasonForIneligibility).contains(PrsIneligibilityReason.ESCAPE)
    Assertions.assertThat(prisonerPrsEligibility.isEligible).isFalse()
  }

  @Test
  fun testCalculateWithMultipleReasons() {
    val prisonerPrsEligibilityCalculator = PrisonerPrsEligibilityCalculator(
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
        .withReleaseDate(LocalDate.now().plusMonths(13))
        .build(),
      emptyList(),
    )
    val prisonerPrsEligibility = prisonerPrsEligibilityCalculator.calculate()
    Assertions.assertThat(prisonerPrsEligibility.reasonForIneligibility).containsAll(
      listOf(
        PrsIneligibilityReason.ESCAPE,
        PrsIneligibilityReason.CATEGORY,
        PrsIneligibilityReason.INCENTIVE_LEVEL,
        PrsIneligibilityReason.TIME_LEFT_TO_SERVE,
      ),
    )
    Assertions.assertThat(prisonerPrsEligibility.isEligible).isFalse()
  }

  @Test
  fun testCalculateIsEligibleWithEscapeAlertThatIsExpired() {
    val prisonerPrsEligibilityCalculator = PrisonerPrsEligibilityCalculator(
      (TestPrisonerFactory())
        .withCategory(Prisoner.CATEGORY_C)
        .withCurrentIncentive(CurrentIncentive(Level(Prisoner.INCENTIVE_LEVEL_STANDARD, "Standard")))
        .withAlerts(
          listOf(
            Alert(
              Alert.ESCAPE_RISK_ALERT_CODE,
              true,
              true,
            ),
          ),
        )
        .withReleaseDate(LocalDate.now().plusMonths(6))
        .build(),
      emptyList(),
    )
    val prisonerPrsEligibility = prisonerPrsEligibilityCalculator.calculate()
    Assertions.assertThat(prisonerPrsEligibility.reasonForIneligibility).isEmpty()
    Assertions.assertThat(prisonerPrsEligibility.isEligible).isTrue()
  }
}
