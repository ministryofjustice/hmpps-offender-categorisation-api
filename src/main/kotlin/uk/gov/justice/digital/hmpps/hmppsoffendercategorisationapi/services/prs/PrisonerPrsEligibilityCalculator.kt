package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.prs

import org.slf4j.LoggerFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.enum.PrsIneligibilityReason
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.enum.SdsExemptionSchedulePart
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.prs.PrisonerPrsEligibility
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.Prisoner
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.SdsExcludedOffenceCode
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.prisoner.Alert
import java.time.LocalDate

class PrisonerPrsEligibilityCalculator(
  private val prisoner: Prisoner,
  private val sdsExcludedOffenceCodes: List<SdsExcludedOffenceCode>?,
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

  private fun prisonerSdsExcludedOnScheduleParts(): List<SdsExemptionSchedulePart> {
    val scheduleParts = mutableListOf<SdsExemptionSchedulePart>()

    prisoner.allConvictedOffences?.forEach { convictedOffence ->
      val sdsExcludedOffenceCode = sdsExcludedOffenceCodes?.find { it.offenceCode == convictedOffence.offenceCode }
      if (sdsExcludedOffenceCode?.schedulePart != SdsExemptionSchedulePart.NONE && !scheduleParts.contains(sdsExcludedOffenceCode?.schedulePart)) {
        sdsExcludedOffenceCode?.schedulePart?.let { scheduleParts.add(it) }
      }
    }
    return scheduleParts
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
    prisonerSdsExcludedOnScheduleParts().forEach {
      val ineligibilityReason = when (it) {
        SdsExemptionSchedulePart.SEXUAL -> PrsIneligibilityReason.OFFENCE_SEXUAL
        SdsExemptionSchedulePart.SEXUAL_T3 -> PrsIneligibilityReason.OFFENCE_SEXUAL_T3
        SdsExemptionSchedulePart.DOMESTIC_ABUSE -> PrsIneligibilityReason.OFFENCE_DOMESTIC_ABUSE
        SdsExemptionSchedulePart.DOMESTIC_ABUSE_T3 -> PrsIneligibilityReason.OFFENCE_DOMESTIC_ABUSE_T3
        SdsExemptionSchedulePart.NATIONAL_SECURITY -> PrsIneligibilityReason.OFFENCE_NATIONAL_SECURITY
        SdsExemptionSchedulePart.TERRORISM -> PrsIneligibilityReason.OFFENCE_TERRORISM
        SdsExemptionSchedulePart.MURDER_T3 -> PrsIneligibilityReason.OFFENCE_MURDER_T3
        SdsExemptionSchedulePart.VIOLENT -> PrsIneligibilityReason.OFFENCE_VIOLENCE
        else -> null
      }
      if (ineligibilityReason != null) {
        reasonForIneligibility.add(ineligibilityReason)
      }
    }
    return PrisonerPrsEligibility(
      reasonForIneligibility,
    )
  }

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}
