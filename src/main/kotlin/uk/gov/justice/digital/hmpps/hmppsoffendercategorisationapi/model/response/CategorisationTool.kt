package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response

import kotlinx.serialization.Serializable
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.CatForm
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.LiteCategory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.NextReviewChangeHistory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.SecurityReferral
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.RiskChange

@Serializable
data class CategorisationTool(
  val catForm: CatForm? = null,
  val riskChange: RiskChange? = null,
  val security: SecurityReferral? = null,
  val nextReviewChangeHistory: NextReviewChangeHistory? = null,
  val liteCategory: LiteCategory? = null
)
