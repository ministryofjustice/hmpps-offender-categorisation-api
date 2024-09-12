package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.BaseSarUnitTest

class CatFormTest : BaseSarUnitTest() {

  @Test
  fun `should deserialise risk_profile json field to object class`() {
    val riskProfileObj =
      json.readValue<RiskProfile>(
        Companion.loadTestData("/form/risk_profile.json"),
      )

    val result = json.writeValueAsString(riskProfileObj)
    val expectedResult = Companion.loadExpectedOutput("risk_profile.json")

    assertThat(result).isEqualTo(expectedResult)
  }

  @Test
  fun `should deserialise form_response`() {
    val formResponse = Companion.jsonStringToMap(Companion.loadTestData("/form/form_response.json"))

    val result = json.writeValueAsString(formResponse)
    val expectedResult = Companion.loadExpectedOutput("form_response.json")
    println(result)

    assert(result == expectedResult)
  }

  @Test
  fun `should deserialise form_response with recat section`() {
    val formResponse = Companion.jsonStringToMap(Companion.loadTestData("/form/form_response_recat.json"))

    val result = json.writeValueAsString(formResponse)
    val expectedResult = Companion.loadExpectedOutput("form_response_recat.json")
    println(result)

    assert(result == expectedResult)
  }
}
