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
      offenderNo = securityReferral.offenderNo,
      status = securityReferral.status,
      processedDate = securityReferral.processedDate.toString(),
    )
  }

  return null
}

fun transformRiskChangeList(riskChange: List<RiskChangeEntity>?): List<RiskChange>? {
  if (riskChange != null) {
    val response = ArrayList<RiskChange>()

    riskChange.forEach {
        response.add(
          RiskChange(
            prisonId = it.prisonId,
            raisedDate = it.raisedDate.toString(),
            offenderNo = it.offenderNo,
            status = it.status,
            oldProfile = it.oldProfile?.let { objectMapper.readValue<Profile>(it) },
            newProfile = it.newProfile?.let { objectMapper.readValue<Profile>(it) },
          )
        )
      }
    return response
  }

  return null
}

/**
 * changedBy is REDACTED
 */
fun transformNextReviewChangeHistoryList(entity: List<NextReviewChangeHistoryEntity>?): List<NextReviewChangeHistory>? {
  if (entity != null) {
    val response = ArrayList<NextReviewChangeHistory>()

    entity.forEach{
        response.add(
        NextReviewChangeHistory(
          reason = it.reason,
          offenderNo = it.offenderNo,
          nextReviewDate = it.nextReviewDate.toString(),
          changeDate = it.changeDate.toString(),
          )
        )
    }

    return response
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
      offenderNo = entity.offenderNo,
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
      offenderNo = entity.offenderNo,
      status = entity.status,
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
      catType = entity.catType,
    )
  }

  return null
}

/**
 * soc, escape, extremism is REDACTED
 */
fun transform(entity: PreviousProfileEntity?): RiskProfiler? {
  if (entity != null) {
    return RiskProfiler(
      offenderNo = entity.offenderNo,
      soc = RedactedSection(),
      escape = RedactedSection(),
      extremism = RedactedSection(),
      violence = entity.violence.let { objectMapper.readValue<Violence>(it) },
      executeDateTime = entity.executeDateTime.toString(),
    )
  }

  return null
}
