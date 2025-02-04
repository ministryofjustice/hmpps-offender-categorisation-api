package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.SarResponse
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.transform
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.transformAllFromCatForm
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.transformLiteCategory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.transformNextReviewChangeHistory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.transformRiskChange
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.transformSecurityReferral
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.FormRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.LiteCategoryRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.NextReviewChangeHistoryRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.RiskChangeRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.SecurityReferralRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.riskprofiler.PreviousProfileRepository
import uk.gov.justice.hmpps.kotlin.sar.HmppsPrisonSubjectAccessRequestService
import uk.gov.justice.hmpps.kotlin.sar.HmppsSubjectAccessRequestContent
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

@Transactional(readOnly = true)
@Service
class SubjectAccessRequestService(
  private val securityReferralRepository: SecurityReferralRepository,
  private val nextReviewChangeHistoryRepository: NextReviewChangeHistoryRepository,
  private val riskChangeRepository: RiskChangeRepository,
  private val liteCategoryRepository: LiteCategoryRepository,
  private val formRepository: FormRepository,
  private val previousProfileRepository: PreviousProfileRepository,
) : HmppsPrisonSubjectAccessRequestService {
  override fun getPrisonContentFor(
    prn: String,
    fromDate: LocalDate?,
    toDate: LocalDate?,
  ): HmppsSubjectAccessRequestContent? {
    val fromZonedDateTime = fromDate?.atStartOfDay(ZoneId.systemDefault())
    val toZonedDateTime = toDate?.atTime(LocalTime.MAX)?.atZone(ZoneId.systemDefault())

    val catFormEntity = formRepository.findAllByOffenderNoOrderBySequenceNoAsc(prn).filter {
      dateIsWithinDates(fromZonedDateTime, toZonedDateTime, it.startDate.atZone(ZoneId.systemDefault())) ||
        dateIsWithinDates(fromZonedDateTime, toZonedDateTime, it.approvalDate?.atStartOfDay(ZoneId.systemDefault()))
    }

    if (catFormEntity.isEmpty()) {
      return null // returns 204 no content - see acceptance criteria si-841
    }

    val previousRiskProfile = previousProfileRepository.findByOffenderNo(prn)

    return HmppsSubjectAccessRequestContent(
      content =
      SarResponse(
        security = transformSecurityReferral(
          securityReferralRepository.findByOffenderNoOrderByRaisedDateDesc(prn).filter {
            dateIsWithinDates(fromZonedDateTime, toZonedDateTime, it.raisedDate)
          },
        ),
        liteCategory = transformLiteCategory(
          liteCategoryRepository.findAllByOffenderNoOrderBySequenceDesc(
            prn,
          ).filter {
            dateIsWithinDates(fromZonedDateTime, toZonedDateTime, it.createdDate) ||
              dateIsWithinDates(fromZonedDateTime, toZonedDateTime, it.approvedDate)
          },
        ),
        riskChange = transformRiskChange(
          riskChangeRepository.findByOffenderNoOrderByRaisedDateDesc(prn).filter {
            println(it.raisedDate)
            dateIsWithinDates(fromZonedDateTime, toZonedDateTime, it.raisedDate)
          },
        ),
        nextReviewChangeHistory = transformNextReviewChangeHistory(
          nextReviewChangeHistoryRepository.findByOffenderNo(prn).filter {
            dateIsWithinDates(fromZonedDateTime, toZonedDateTime, it.changeDate)
          },
        ),
        catForm = transformAllFromCatForm(catFormEntity),
        riskProfiler = if (dateIsWithinDates(fromZonedDateTime, toZonedDateTime, previousRiskProfile.executeDateTime)) transform(previousRiskProfile) else null,
      ),
    )
  }

  private fun dateIsWithinDates(fromDate: ZonedDateTime?, toDate: ZonedDateTime?, dateInQuestion: ZonedDateTime?): Boolean {
    return (fromDate == null || (dateInQuestion == null || dateInQuestion.isAfter(fromDate))) &&
      (toDate == null || (dateInQuestion == null || dateInQuestion.isBefore(toDate)))
  }
}
