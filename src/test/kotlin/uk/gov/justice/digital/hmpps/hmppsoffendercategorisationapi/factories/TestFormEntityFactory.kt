package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories

import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.FormEntity
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.enum.CatType
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.enum.ReviewReason
import java.time.LocalDate
import java.time.LocalDateTime

class TestFormEntityFactory {
  private var bookingId = 1L
  private var formResponse: String? = null
  private var status = FormEntity.STATUS_APPROVED
  private var prisonId = "BXI"
  private var offenderNo = "ABC123"
  private var startDate = LocalDateTime.of(2024, 1, 1, 0, 0, 0)
  private var securityReviewedDate = LocalDateTime.of(2024, 2, 1, 0, 0, 0)
  private var securityReviewedBy: String? = null
  private var approvalDate = LocalDate.of(2024, 3, 1)
  private var catType = CatType.INITIAL
  private var nomisSequenceNo = 0
  private var assessmentDate = LocalDate.of(2024, 4, 1)
  private var reviewReason = ReviewReason.MANUAL
  private var dueByDate = LocalDate.of(2024, 4, 1)
  private var cancelledDate: LocalDateTime? = null
  private var cancelledBy = ""

  fun withBookingId(bookingId: Long): TestFormEntityFactory {
    this.bookingId = bookingId
    return this
  }

  fun withFormResponse(formResponse: String): TestFormEntityFactory {
    this.formResponse = formResponse
    return this
  }

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

  fun withStartDate(startDate: LocalDateTime): TestFormEntityFactory {
    this.startDate = startDate
    return this
  }

  fun withSecurityReviewedDate(securityReviewedDate: LocalDateTime): TestFormEntityFactory {
    this.securityReviewedDate = securityReviewedDate
    return this
  }

  fun withSecurityReviewedBy(securityReviewedBy: String): TestFormEntityFactory {
    this.securityReviewedBy = securityReviewedBy
    return this
  }

  fun withApprovalDate(approvalDate: LocalDate?): TestFormEntityFactory {
    this.approvalDate = approvalDate
    return this
  }

  fun withCatType(catType: CatType): TestFormEntityFactory {
    this.catType = catType
    return this
  }

  fun withNomisSequenceNo(nomisSequenceNo: Int): TestFormEntityFactory {
    this.nomisSequenceNo = nomisSequenceNo
    return this
  }

  fun withAssessmentDate(assessmentDate: LocalDate): TestFormEntityFactory {
    this.assessmentDate = assessmentDate
    return this
  }

  fun withReviewReason(reviewReason: ReviewReason): TestFormEntityFactory {
    this.reviewReason = reviewReason
    return this
  }

  fun withDueDate(dueByDate: LocalDate): TestFormEntityFactory {
    this.dueByDate = dueByDate
    return this
  }

  fun withCancelledDate(cancelledDate: LocalDateTime): TestFormEntityFactory {
    this.cancelledDate = cancelledDate
    return this
  }

  fun withCancelledBy(cancelledBy: String): TestFormEntityFactory {
    this.cancelledBy = cancelledBy
    return this
  }

  fun build(): FormEntity {
    return FormEntity(
      bookingId = this.bookingId,
      formResponse = formResponse,
      status = this.status,
      prisonId = this.prisonId,
      offenderNo = this.offenderNo,
      startDate = this.startDate,
      securityReviewedDate = this.securityReviewedDate,
      securityReviewedBy = this.securityReviewedBy,
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
