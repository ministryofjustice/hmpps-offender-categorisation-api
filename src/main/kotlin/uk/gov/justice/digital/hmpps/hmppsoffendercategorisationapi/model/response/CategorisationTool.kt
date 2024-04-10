package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response

import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.CatForm
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.LiteCategory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.NextReviewChangeHistory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.SecurityReferral
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.RiskChange

data class CategorisationTool(
  val catForm: CatForm?,
  val riskChange: RiskChange?,
  val security: SecurityReferral?,
  val nextReviewChangeHistory: NextReviewChangeHistory,
  val liteCategory: LiteCategory
)
