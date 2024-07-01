package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response

import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.common.RedactedSection

class SarResponseTest : CategorisationToolTest() {

  @Test
  fun `should build response to match response defined in acceptance criteria`() {
    val sarResponse = json.writeValueAsString(sarResponse)

    val expectedResult = loadExpectedOutput("sar_response.json")

    println(sarResponse)

    assertThat(sarResponse).isEqualTo(expectedResult)
  }

  fun `should deserialise payload from acceptance criteria`() {
    val sarResponseObj =
      json.writeValueAsString(
        loadTestData("CAT Subject Access Request API data.json"),
      )

    val expectedResult = loadExpectedOutput("sar_response_from_acceptance_criteria.json")

    val result = json.writeValueAsString(sarResponseObj)
    println(result)
    assertThat(result).isEqualTo(expectedResult)
  }

  @Test
  fun `should allow empty json for sar response`() {
    val emptyResponse = json.writeValueAsString(SarResponse())

    assert(emptyResponse == "{}")
  }

  protected companion object {
    protected val sarResponse = SarResponse(
      categorisationTool,
      riskProfiler = RiskProfiler(
        offenderNo = "G2194GK",
        escape = RedactedSection(),
        extremism = RedactedSection(),
        soc = RedactedSection(),
        violence = riskProfilerViolence,
        executeDateTime = "2021-07-27 02:18:22.10621",
      ),
    )
  }
}
