package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.FormEntity
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.LiteCategoryEntity
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.NextReviewChangeHistoryEntity
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.RiskChangeEntity
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.SecurityReferralEntity
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.CatForm
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.LiteCategory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.NextReviewChangeHistory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.RiskProfile
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.SecurityReferral
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Profile
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.RiskChange

fun transform(securityReferral: SecurityReferralEntity): SecurityReferral {
  return SecurityReferral(
    prisonId = securityReferral.prisonId,
    raisedDate = securityReferral.raisedDate.toString(),
    offenderNo = securityReferral.offenderNo,
    status = securityReferral.status,
    processedDate = securityReferral.processedDate.toString(),
  )
}

private val json = Json { ignoreUnknownKeys = true }

fun transform(riskChange: RiskChangeEntity): RiskChange {
  return RiskChange(
    prisonId = riskChange.prisonId,
    raisedDate = riskChange.raisedDate.toString(),
    offenderNo = riskChange.offenderNo,
    status = riskChange.status,
    oldProfile = riskChange.oldProfile?.let { json.decodeFromString<Profile>(it) },
    newProfile = riskChange.newProfile?.let { json.decodeFromString<Profile>(it) },
  )
}

/**
 * changedBy is REDACTED
 */
fun transform(entity: NextReviewChangeHistoryEntity): NextReviewChangeHistory {
  return NextReviewChangeHistory(
    bookingId = entity.bookingId,
    reason = entity.reason,
    offenderNo = entity.offenderNo,
    nextReviewDate = entity.nextReviewDate.toString(),
    changeDate = entity.changeDate.toString(),
  )
}

/**
 * assessedBy,approvedBy is REDACTED
 */
fun transform(entity: LiteCategoryEntity): LiteCategory {
  return LiteCategory(
    bookingId = entity.bookingId,
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

/**
 * cancelledBy is REDACTED
 */
fun transform(entity: FormEntity): CatForm {
  return CatForm(
    prisonId = entity.prisonId,
    offenderNo = entity.offenderNo,
    status = entity.status,
    bookingId = entity.bookingId.toString(),
    reviewReason = entity.reviewReason,
    dueByDate = entity.dueByDate,

    formResponse = entity.formResponse?.let { json.decodeFromString<Map<String, JsonElement>>(it) },
    riskProfile = entity.riskProfile?.let { json.decodeFromString<RiskProfile>(it) },

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
