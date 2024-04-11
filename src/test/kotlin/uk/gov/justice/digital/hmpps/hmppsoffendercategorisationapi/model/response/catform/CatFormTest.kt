package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
class CatFormTest: BaseSarUnitTest()  {
  @Test
  fun `should match acceptance criteria test data`() {

    val str = Json.encodeToString(catForm)

    System.out.println(str)

  }

  @Test
  fun `should deserialise risk_profile json field to object class`() {

    val riskProfileObj =
      Json{ ignoreUnknownKeys = true }.decodeFromString<RiskProfile>(
        Companion.loadTestData("risk_profile.json")
      )

    val result = Json.encodeToString(riskProfileObj)
    val expectedResult = Companion.loadExpectedOutput("risk_profile.json")

    assert(result == expectedResult)

    println(result)
  }
}