package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response

open class CategorisationToolTest : BaseSarUnitTest() {

  protected companion object {

    @JvmStatic
    protected val categorisationTool = CategorisationTool(
      catForm,
      security = listOf(security),
      liteCategory = listOf(liteCategory),
      nextReviewChangeHistory  = listOf(nextReviewChangeHistory),
      riskChange = listOf(riskChange),
    )
  }
}
