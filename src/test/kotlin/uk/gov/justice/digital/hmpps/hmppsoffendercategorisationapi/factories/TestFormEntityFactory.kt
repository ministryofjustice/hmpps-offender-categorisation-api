package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories

import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.FormEntity

class TestFormEntityFactory {
  private var status = FormEntity.STATUS_APPROVED
  private var prisonId = "BXI"
  private var offenderNo = "ABC123"
  private var startDate = "2024-01-01"
  private var securityReviewedDate = "2024-02-01"
  private var approvalDate = "2024-03-01"
  private var catType = FormEntity.CAT_TYPE_INITIAL
  private var nomisSequenceNo = "0"
  private var assessmentDate = "2024-04-01"
  private var reviewReason = FormEntity.REVIEW_REASON_MANUAL
  private var dueByDate = "2024-03-01"
  private var cancelledDate = ""
  private var cancelledBy = ""

  fun withStatus(status: String): TestFormEntityFactory {
    this.status = status
    return this
  }

  fun withPrisonId(prisonId: String): TestFormEntityFactory {
    this.prisonId = prisonId
    return this
  }

  fun withOffenderNo(offenderNo: String): TestFormEntityFactory {
    this.offenderNo = offenderNo
    return this
  }

  fun withStartDate(startDate: String): TestFormEntityFactory {
    this.startDate = startDate
    return this
  }

  fun withSecurityReviewedDate(securityReviewedDate: String): TestFormEntityFactory {
    this.securityReviewedDate = securityReviewedDate
    return this
  }

  fun withApprovalDate(approvalDate: String): TestFormEntityFactory {
    this.approvalDate = approvalDate
    return this
  }

  fun withCatType(catType: String): TestFormEntityFactory {
    this.catType = catType
    return this
  }

  fun withNomisSequenceNo(nomisSequenceNo: String): TestFormEntityFactory {
    this.nomisSequenceNo = nomisSequenceNo
    return this
  }

  fun withAssessmentDate(assessmentDate: String): TestFormEntityFactory {
    this.assessmentDate = assessmentDate
    return this
  }

  fun withReviewReason(reviewReason: String): TestFormEntityFactory {
    this.reviewReason = reviewReason
    return this
  }

  fun withDueDate(dueByDate: String): TestFormEntityFactory {
    this.dueByDate = dueByDate
    return this
  }

  fun withCancelledDate(cancelledDate: String): TestFormEntityFactory {
    this.cancelledDate = cancelledDate
    return this
  }

  fun withCancelledBy(cancelledBy: String): TestFormEntityFactory {
    this.cancelledBy = cancelledBy
    return this
  }

  fun build(): FormEntity {
    return FormEntity(
      status = this.status,
      prisonId = this.prisonId,
      offenderNo = this.offenderNo,
      startDate = this.startDate,
      securityReviewedDate = this.securityReviewedDate,
      approvalDate = this.approvalDate,
      catType = this.catType,
      nomisSequenceNo = this.nomisSequenceNo,
      assessmentDate = this.assessmentDate,
      reviewReason = this.reviewReason,
      dueByDate = this.dueByDate,
      cancelledDate = this.cancelledDate,
      cancelledBy = this.cancelledBy,
    )
  }
}
