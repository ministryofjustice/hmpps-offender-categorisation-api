package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

class CatFormTest : BaseSarUnitTest() {
  @Test
  fun `should match acceptance criteria example response data`() {

    val result = Json.encodeToString(catForm)
    val expectedResult = Companion.loadExpectedOutput("catForm.json")

    println(result)

    assert(result == expectedResult)
  }

  @Test
  fun `should deserialise risk_profile json field to object class`() {
    val riskProfileObj =
      Json { ignoreUnknownKeys = true }.decodeFromString<RiskProfile>(
        Companion.loadTestData("/form/risk_profile.json"),
      )

    val result = Json.encodeToString(riskProfileObj)
    val expectedResult = Companion.loadExpectedOutput("risk_profile.json")

    assert(result == expectedResult)

    println(result)
  }

  @Test
  fun `should deserialise form_response`() {
    val formResponse = Companion.jsonStringToMap(Companion.loadTestData("/form/form_response.json"))

    val result = Json.encodeToString(formResponse)
    val expectedResult = Companion.loadExpectedOutput("form_response.json")
    println(result)

    assert(result == expectedResult)
  }

  @Test
  fun `should deserialise form_response with recat section`() {
    val formResponse = Companion.jsonStringToMap(Companion.loadTestData("/form/form_response_recat.json"))

    val result = Json.encodeToString(formResponse)
    val expectedResult = Companion.loadExpectedOutput("form_response_recat.json")
    println(result)

    assert(result == expectedResult)
  }
}
