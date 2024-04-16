package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.CategorisationTool
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.SarResponse
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.transform
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.*
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
) : HmppsPrisonSubjectAccessRequestService {

  companion object {
    val minDate: LocalDate = LocalDate.EPOCH
    val maxDate: LocalDate = LocalDate.now()
  }

  override fun getPrisonContentFor(
    prn: String,
    fromDate: LocalDate?,
    toDate: LocalDate?,
  ): HmppsSubjectAccessRequestContent? {

    return HmppsSubjectAccessRequestContent(
      content = SarResponse(
        categorisationTool = CategorisationTool(
          security = transform(securityReferralRepository.findByOffenderNo(prn)),
          liteCategory = transform(liteCategoryRepository.findByOffenderNo(prn)),
          riskChange = transform(riskChangeRepository.findByOffenderNo(prn)),
          nextReviewChangeHistory = transform(nextReviewChangeHistoryRepository.findByOffenderNo(prn)),
          catForm = transform(formRepository.findByOffenderNo(prn)),
        ),
        riskProfiler = null,
      )
    )
  }
}
