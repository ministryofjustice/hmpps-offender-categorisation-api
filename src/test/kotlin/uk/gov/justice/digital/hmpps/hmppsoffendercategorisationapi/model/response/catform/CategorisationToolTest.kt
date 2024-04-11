package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.CategorisationTool
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.riskprofile.LifeProfile
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.common.RedactedSection
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Escape
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Violence
import java.time.LocalDate.now
import java.time.ZonedDateTime


class CategorisationToolTest: BaseSarUnitTest()  {
  @Test
  fun `should match acceptance criteria test data`() {

    val str = Json.encodeToString(categorisationTool)

    System.out.println(str)

  }

  @Test
  fun `should allow empty json for cat tool`() {

    val str = Json.encodeToString(emptyCategorisationTool)

    System.out.println(str)

  }

  protected companion object {
    protected val emptyCategorisationTool = CategorisationTool(
      catForm = null,
      nextReviewChangeHistory = null,
      security = null,
      liteCategory = null,
      riskChange = null
    )

    protected val categorisationTool = CategorisationTool(
      catForm = catForm,
      nextReviewChangeHistory = NextReviewChangeHistory(
        id = "1",
        bookingId = "1146373",
        offenderNo = "G7919UD",
        nextReviewDate = "2021-12-12",
        reason = "testing",
        changeDate = "2021-09-22 10:25:44.395 +0100"
      ),
      security = null,
      liteCategory = null,
      riskChange = null
    )
  }
}