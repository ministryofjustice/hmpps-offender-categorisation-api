package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.CategorisationTool
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.SarResponse
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.transform
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.FormRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.LiteCategoryRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.NextReviewChangeHistoryRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.RiskChangeRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.PreviousProfileRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.SecurityReferralRepository
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
    return HmppsSubjectAccessRequestContent(
      content =
      SarResponse(
        categorisationTool = CategorisationTool(
          security = transform(securityReferralRepository.findByOffenderNo(prn)),
          liteCategory = transform(liteCategoryRepository.findByOffenderNo(prn)),
          riskChange = transform(riskChangeRepository.findByOffenderNo(prn)),
          nextReviewChangeHistory = transform(nextReviewChangeHistoryRepository.findByOffenderNo(prn)),
          catForm = transform(formRepository.findByOffenderNo(prn)),
        ),
        riskProfiler = transform(previousProfileRepository.findByOffenderNo(prn)),
      ),
    )
  }
}
