package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.BaseSarUnitTest
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.RiskChange

class RiskChangeTest : BaseSarUnitTest() {
  private val json = Json { ignoreUnknownKeys = true }

  @Test
  fun `should match acceptance criteria test data`() {

    val str = json.encodeToString(riskChange)

    println(str)

  }

  @Test
  fun `should deserialise risk_change table with only old and new profile json blob field to object class`() {

    val riskProfileObj =
      Json { ignoreUnknownKeys = true }.decodeFromString<RiskChange>(
        Companion.loadTestData("tables/risk_change_table.json"),
      )

    val result = Json.encodeToString(riskProfileObj)
    val expectedResult = Companion.loadExpectedOutput("risk_change_table.json")


    println(result)

    assert(result == expectedResult)

  }
}