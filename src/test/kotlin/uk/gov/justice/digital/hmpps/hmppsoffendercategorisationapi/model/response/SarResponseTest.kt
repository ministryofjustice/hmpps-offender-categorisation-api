package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.common.RedactedSection

class SarResponseTest : CategorisationToolTest() {
  private val json = Json { ignoreUnknownKeys = true }

  @Test
  fun `should build response to match response defined in acceptance criteria`() {
    val sarResponse = Json.encodeToString(sarResponse)

    val expectedResult = loadExpectedOutput("sar_response.json")

    println(sarResponse)

    assert(sarResponse == expectedResult)
  }

  @Test
  fun `should deserialise payload from acceptance criteria`() {
    val sarResponseObj =
      json.decodeFromString<SarResponse>(
        loadTestData("CAT Subject Access Request API data.json"),
      )

    val expectedResult = loadExpectedOutput("sar_response_from_acceptance_criteria.json")

    val result = Json.encodeToString(sarResponseObj)
    println(result)
    assert(result == expectedResult)
  }

  @Test
  fun `should allow empty json for sar response`() {
    val emptyResponse = Json.encodeToString(SarResponse())

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
