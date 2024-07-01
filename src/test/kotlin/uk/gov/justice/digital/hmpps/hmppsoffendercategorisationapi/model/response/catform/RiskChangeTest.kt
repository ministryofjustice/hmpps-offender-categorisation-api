package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.BaseSarUnitTest
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.RiskChange

class RiskChangeTest : BaseSarUnitTest() {

  @Test
  fun `should deserialise risk_change table with only old and new profile json blob field to object class`() {
    val riskProfileObj =
      json.readValue<RiskChange>(
        Companion.loadTestData("tables/risk_change_table.json"),
      )

    val result = json.writeValueAsString(riskProfileObj)
    val expectedResult = Companion.loadExpectedOutput("risk_change_table.json")

    println(result)

    assert(result == expectedResult)
  }
}
