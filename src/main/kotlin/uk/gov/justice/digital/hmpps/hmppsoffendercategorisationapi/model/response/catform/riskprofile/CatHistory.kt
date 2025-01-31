package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.riskprofile

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class CatHistory(
  bookingId: Int,
  offenderNo: String,
  approvalDate: String,
  assessmentSequence: Int,
  assessmentDate: String,
  classification: String,
  nextReviewDate: String,
  assessmentStatus: String,
  agencyDescription: String,
  assessmentComment: String,
  classificationCode: String,
  approvalDateDisplay: String,
  cellSharingAlertFlag: Boolean,
  assessmentDescription: String,
)
