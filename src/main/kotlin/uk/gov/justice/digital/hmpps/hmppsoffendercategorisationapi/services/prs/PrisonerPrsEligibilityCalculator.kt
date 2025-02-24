package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.prs

import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.enum.PrsIneligibilityReason
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.prs.PrisonerPrsEligibility
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.Prisoner
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.prisoner.Alert
import java.time.LocalDate

class PrisonerPrsEligibilityCalculator(
  private val prisoner: Prisoner,
  private val sdsExcludedOffenceCodes: List<String>,
) {
  private fun isCategoryCOrClosed(): Boolean {
    return this.prisoner.category == Prisoner.CATEGORY_C || this.prisoner.category == Prisoner.CATEGORY_R
  }

  private fun hasOneToTwelveMonthsLeftToServeBeforeCrd(): Boolean {
    return prisoner.releaseDate?.isAfter(LocalDate.now().plusMonths(1)) ?: false &&
      prisoner.releaseDate?.isBefore(LocalDate.now().plusMonths(12)) ?: false
  }

  private fun hasEnhancedOrStandardIncentiveLevel(): Boolean {
    return listOf(
      Prisoner.INCENTIVE_LEVEL_STANDARD,
      Prisoner.INCENTIVE_LEVEL_ENHANCED,
      Prisoner.INCENTIVE_LEVEL_ENHANCED_TWO,
      Prisoner.INCENTIVE_LEVEL_ENHANCED_THREE,
    ).contains(prisoner.currentIncentive?.level?.code)
  }

  private fun hasEscapeFlag(): Boolean {
    return this.prisoner.alerts?.any {
      listOf(
        Alert.ESCAPE_RISK_ALERT_CODE,
        Alert.ESCAPE_LIST_ALERT_CODE,
        Alert.ESCAPE_LIST_HEIGHTENED_ALERT_CODE,
      ).contains(it.alertCode) && it.isActiveAndNonExpired
    } ?: false
  }

  private fun prisonersSdsExcludedOffenceCodes(): List<String> {
    return prisoner.allConvictedOffences?.map { it.offenceCode }?.filter { sdsExcludedOffenceCodes.contains(it) } ?: emptyList()
  }

  fun calculate(): PrisonerPrsEligibility {
    val reasonForIneligibility = mutableListOf<PrsIneligibilityReason>()
    if (!isCategoryCOrClosed()) {
      reasonForIneligibility.add(PrsIneligibilityReason.CATEGORY)
    }
    if (!hasOneToTwelveMonthsLeftToServeBeforeCrd()) {
      reasonForIneligibility.add(PrsIneligibilityReason.TIME_LEFT_TO_SERVE)
    }
    if (!hasEnhancedOrStandardIncentiveLevel()) {
      reasonForIneligibility.add(PrsIneligibilityReason.INCENTIVE_LEVEL)
    }
    if (hasEscapeFlag()) {
      reasonForIneligibility.add(PrsIneligibilityReason.ESCAPE)
    }
    return PrisonerPrsEligibility(
      reasonForIneligibility,
      prisonersSdsExcludedOffenceCodes(),
    )
  }
}
