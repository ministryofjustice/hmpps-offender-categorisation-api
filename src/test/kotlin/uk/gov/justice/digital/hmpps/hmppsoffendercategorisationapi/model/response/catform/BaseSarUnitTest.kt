package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.riskprofile.LifeProfile
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.common.RedactedSection
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Escape
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Profile
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.RiskChange
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Violence
import java.io.File
import java.io.InputStream


open class BaseSarUnitTest {

  protected companion object {

    fun loadTestData(filename: String): String {
      val inputStream: InputStream = File("src/test/resources/testdata/$filename").inputStream()
      return inputStream.bufferedReader().use { it.readText() }
    }

    fun loadExpectedOutput(filename: String): String {
      val inputStream: InputStream = File("src/test/resources/expectedoutput/$filename").inputStream()
      return inputStream.bufferedReader().use { it.readText() }
    }

    fun jsonStringToMap(json: String): Map<String, JsonElement> {
      val data = Json.parseToJsonElement(json)
      require(data is JsonObject) { "Only Json Objects can be converted to a Map!" }
      return data
    }

    protected val riskProfile = RiskProfile(
      history = RedactedSection(),
      offences = null,
      socProfile = RedactedSection(),
      lifeProfile = LifeProfile(
        life = true,
        nomsId = "G8105VR",
        riskType = "LIFE",
        provisionalCategorisation = "B",
      ),
      escapeProfile = Escape(
        nomsId = "G8105VR",
        riskType = "ESCAPE",
        provisionalCategorisation = "C",
      ),
      violenceProfile = Violence(
        nomsId = "G8105VR",
        riskType = "VIOLENCE",
        displayAssaults = false,
        numberOfAssaults = 0,
        notifySafetyCustodyLead = false,
        numberOfSeriousAssaults = 0,
        provisionalCategorisation = "C",
        numberOfNonSeriousAssaults = 0,
        veryHighRiskViolentOffender = false,
      ),
      extremismProfile = RedactedSection(),
    )

    @JvmStatic
    protected val catForm = CatForm(
      id = "1598",
      formResponse = Companion.jsonStringToMap(Companion.loadTestData("/form/form_response.json")),
      bookingId = "771697",
      status = "APPROVED",
      referredDate = "2023-03-21 15:08:50.982 +0000",
      sequenceNo = "1",
      riskProfile = riskProfile,
      prisonId = "WMI",
      offenderNo = "G8105VR",
      startDate = "2023-03-21 15:09:00.266 +0000",
      securityReviewedDate = "2023-03-21 15:09:00.266 +0000",
      approvalDate = "2023-03-21",
      catType = "INITIAL",
      nomisSequenceNo = "1900-01-19",
      assessmentDate = "2023-03-21",
      reviewReason = "MANUAL",
      dueByDate = "1977-12-16",
      cancelledDate = "1977-12-17",
    )

    @JvmStatic
    protected val riskChange = RiskChange(
      id = "1",
      oldProfile = Profile(
        soc = null,
        escape = Escape(
          nomsId = "G0048VL",
          riskType = "ESCAPE",
          provisionalCategorisation = "C",
        ),
        violence = Violence(
          nomsId = "G0048VL",
          riskType = "VIOLENCE",
          displayAssaults = true,
          numberOfAssaults = 0,
          provisionalCategorisation = "C",
          veryHighRiskViolentOffender = false,
        ),
        extremism = null,
      ),
      newProfile = Profile(
        soc = null,
        escape = Escape(
          nomsId = "G0048VL",
          riskType = "ESCAPE",
          provisionalCategorisation = "C",
        ),
        violence = Violence(
          nomsId = "G0048VL",
          riskType = "VIOLENCE",
          displayAssaults = true,
          numberOfAssaults = 0,
          provisionalCategorisation = "C",
          veryHighRiskViolentOffender = false,
        ),
        extremism = null,
      ),
      offenderNo = "",
      prisonId = "BAI",
      status = "REVIEWED_FIRST",
      raisedDate = "2019-09-18 10:45:34.166 +0100"
    )
  }
}