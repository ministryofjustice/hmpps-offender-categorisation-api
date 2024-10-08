package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response

import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.NextReviewChangeHistory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.RiskChange

open class CategorisationToolTest : BaseSarUnitTest() {

  protected companion object {
    @JvmStatic
    protected val categorisationTool = CategorisationTool(
      catForm = catForm,
      nextReviewChangeHistory = emptyList<NextReviewChangeHistory>().plus(
        NextReviewChangeHistory(
          id = "1",
          offenderNo = "G7919UD",
          nextReviewDate = "2021-12-12",
          reason = "testing",
          changeDate = "2021-09-22 10:25:44.395 +0100",
        ),
      ),
      security = security,
      liteCategory = liteCategory,
      riskChange = emptyList<RiskChange>().plus(riskChange),
    )
  }
}
