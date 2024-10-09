package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.*
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.CategorisationTool
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.SarResponse
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.FormRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.LiteCategoryRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.NextReviewChangeHistoryRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.RiskChangeRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.SecurityReferralRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.riskprofiler.PreviousProfileRepository
import uk.gov.justice.hmpps.kotlin.sar.HmppsPrisonSubjectAccessRequestService
import uk.gov.justice.hmpps.kotlin.sar.HmppsSubjectAccessRequestContent
import java.time.LocalDate

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
    val catFormEntity = formRepository.findTopByOffenderNoOrderBySequenceNoAsc(prn)

    if (catFormEntity == null) {
      return null // returns 204 no content - see acceptance criteria si-841
    }

    return HmppsSubjectAccessRequestContent(
      content =
      SarResponse(
        categorisationTool = CategorisationTool(
          security = transformSecurityReferral(securityReferralRepository.findByOffenderNoOrderByRaisedDateDesc(prn)),
          liteCategory = transformLiteCategory(liteCategoryRepository.findByOffenderNoOrderBySequenceDesc(prn)),
          riskChange = transformRiskChange(riskChangeRepository.findByOffenderNoOrderByRaisedDateDesc(prn)),
          nextReviewChangeHistory = transformNextReviewChangeHistory(nextReviewChangeHistoryRepository.findByOffenderNo(prn)),
          catForm = transform(catFormEntity),
        ),
        riskProfiler = transform(previousProfileRepository.findByOffenderNo(prn)),
      ),
    )
  }
}
