package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.FormEntity
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.LiteCategoryEntity
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.NextReviewChangeHistoryEntity
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.RiskChangeEntity
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.SecurityReferralEntity
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.riskprofiler.PreviousProfileEntity
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.RiskProfiler
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.CatForm
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.LiteCategory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.NextReviewChangeHistory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.RiskProfile
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.SecurityReferral
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.common.RedactedSection
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Profile
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.RiskChange
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Violence

val objectMapper = jacksonObjectMapper()

fun transform(securityReferral: SecurityReferralEntity?): SecurityReferral? {
  if (securityReferral != null) {
    return SecurityReferral(
      prisonId = securityReferral.prisonId,
      raisedDate = securityReferral.raisedDate.toString(),
      status = securityReferral.status,
      processedDate = securityReferral.processedDate.toString(),
    )
  }

  return null
}

fun transform(riskChange: RiskChangeEntity?): RiskChange? {
  if (riskChange != null) {
    return RiskChange(
      prisonId = riskChange.prisonId,
      raisedDate = riskChange.raisedDate.toString(),
      status = riskChange.status,
      oldProfile = riskChange.oldProfile?.let { objectMapper.readValue<Profile>(it) },
      newProfile = riskChange.newProfile?.let { objectMapper.readValue<Profile>(it) },
    )
  }

  return null
}

/**
 * changedBy is REDACTED
 */
fun transform(entity: NextReviewChangeHistoryEntity?): NextReviewChangeHistory? {
  if (entity != null) {
    return NextReviewChangeHistory(
      bookingId = entity.bookingId,
      reason = entity.reason,
      nextReviewDate = entity.nextReviewDate.toString(),
      changeDate = entity.changeDate.toString(),
    )
  }

  return null
}

/**
 * assessedBy,approvedBy is REDACTED
 */
fun transform(entity: LiteCategoryEntity?): LiteCategory? {
  if (entity != null) {
    return LiteCategory(
      prisonId = entity.prisonId,
      category = entity.category,
      sequence = entity.sequence,

      approvedDate = entity.approvedDate.toString(),
      approvedComment = entity.approvedComment,
      approvedCommittee = entity.approvedCommittee,

      assessmentComment = entity.assessmentComment,
      assessmentCommittee = entity.assessmentCommittee,

      approvedPlacementPrisonId = entity.approvedPlacementPrisonId,
      approvedPlacementComment = entity.approvedPlacementComment,

      nextReviewDate = entity.nextReviewDate.toString(),
      createdDate = entity.createdDate.toString(),
      supervisorCategory = entity.supervisorCategory,
      placementPrisonId = entity.placementPrisonId,
    )
  }

  return null
}

/**
 * userId, cancelledBy is REDACTED
 */
fun transform(entity: FormEntity?): CatForm? {
  if (entity != null) {
    return CatForm(
      prisonId = entity.prisonId,
      status = entity.status,
      bookingId = entity.bookingId.toString(),
      reviewReason = entity.reviewReason,
      dueByDate = entity.dueByDate,

      formResponse = entity.formResponse?.let { objectMapper.readValue<Map<String, Any>>(it) },
      riskProfile = entity.riskProfile?.let { objectMapper.readValue<RiskProfile>(it) },

      cancelledDate = entity.cancelledDate,
      approvalDate = entity.approvalDate,
      securityReviewedDate = entity.securityReviewedDate,
      assessmentDate = entity.assessmentDate,
      startDate = entity.startDate,
      referredDate = entity.referredDate,
      nomisSequenceNo = entity.nomisSequenceNo,
      catType = entity.catType,
      sequenceNo = entity.sequenceNo,
    )
  }

  return null
}

/**
 * soc, escape, extremism is REDACTED
 */
fun transform(entity: PreviousProfileEntity?): RiskProfiler? {
  if (entity != null) {

    val violenceConvert = entity.violence.let { objectMapper.readValue<Violence>(it) }

    return RiskProfiler(
      soc = RedactedSection(),
      escape = RedactedSection(),
      extremism = RedactedSection(),
      violence = Violence(
        riskType = violenceConvert.riskType,
        numberOfAssaults = violenceConvert.numberOfAssaults,
        displayAssaults = violenceConvert.displayAssaults,
        provisionalCategorisation = violenceConvert.provisionalCategorisation,
        veryHighRiskViolentOffender = violenceConvert.veryHighRiskViolentOffender,
        numberOfSeriousAssaults = violenceConvert.numberOfSeriousAssaults,
        numberOfNonSeriousAssaults = violenceConvert.numberOfNonSeriousAssaults,
        notifySafetyCustodyLead = violenceConvert.notifySafetyCustodyLead
      ),
      executeDateTime = entity.executeDateTime.toString(),
    )
  }

  return null
}
