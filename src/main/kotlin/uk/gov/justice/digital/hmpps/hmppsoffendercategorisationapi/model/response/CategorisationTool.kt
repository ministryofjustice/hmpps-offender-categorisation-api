package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response

import com.fasterxml.jackson.annotation.JsonInclude
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.CatForm
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.LiteCategory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.NextReviewChangeHistory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.SecurityReferral
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.RiskChange

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CategorisationTool(
  val catForm: List<CatForm>,
  val riskChange: List<RiskChange>? = null,
  val security: List<SecurityReferral>? = null,
  val nextReviewChangeHistory: List<NextReviewChangeHistory>? = null,
  val liteCategory: List<LiteCategory>? = null,
)
