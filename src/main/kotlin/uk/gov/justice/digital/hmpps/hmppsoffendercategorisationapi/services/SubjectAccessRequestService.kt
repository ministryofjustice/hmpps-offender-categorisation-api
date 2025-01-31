package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.CategorisationTool
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
    val catFormEntity = formRepository.findByOffenderNoAndStartDateBetweenOrApprovalDateBetweenOrderBySequenceNoAsc(
      prn,
      fromDate?.atStartOfDay(),
      toDate?.atTime(LocalTime.MAX),
      fromDate,
      toDate,
    )

    if (catFormEntity.isEmpty()) {
      return null // returns 204 no content - see acceptance criteria si-841
    }

    val fromZonedDateTime = fromDate?.atStartOfDay(ZoneId.systemDefault())
    val toZonedDateTime = toDate?.atTime(LocalTime.MAX)?.atZone(ZoneId.systemDefault())

    return HmppsSubjectAccessRequestContent(
      content =
      SarResponse(
        categorisationTool = CategorisationTool(
          security = transformSecurityReferral(
            securityReferralRepository.findByOffenderNoAndRaisedDateBetweenOrderByRaisedDateDesc(
              prn,
              fromZonedDateTime,
              toZonedDateTime,
            ),
          ),
          liteCategory = transformLiteCategory(
            liteCategoryRepository.findByOffenderNoAndCreatedDateBetweenOrApprovedDateBetweenOrderBySequenceDesc(
              prn,
              fromZonedDateTime,
              toZonedDateTime,
              fromZonedDateTime,
              toZonedDateTime,
            ),
          ),
          riskChange = transformRiskChange(
            riskChangeRepository.findByOffenderNoAndRaisedDateBetweenOrderByRaisedDateDesc(
              prn,
              fromZonedDateTime,
              toZonedDateTime,
            ),
          ),
          nextReviewChangeHistory = transformNextReviewChangeHistory(
            nextReviewChangeHistoryRepository.findByOffenderNoAndChangeDateBetween(
              prn,
              fromZonedDateTime,
              toZonedDateTime,
            ),
          ),
          catForm = transformAllFromCatForm(catFormEntity),
        ),
        riskProfiler = transform(
          previousProfileRepository.findByOffenderNoAndExecuteDateTimeBetween(
            prn,
            fromZonedDateTime,
            toZonedDateTime,
          ),
        ),
      ),
    )
  }
}
