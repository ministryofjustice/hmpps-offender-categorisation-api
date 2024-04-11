package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform

import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.riskprofile.LifeProfile
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.common.RedactedSection
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Escape
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Profile
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.RiskChange
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Violence
import java.io.File
import java.io.InputStream
import java.time.LocalDate.now
import java.time.ZonedDateTime


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
      formResponse = FormResponse(
        id = 1,
      ),
      bookingId = "771697",
      status = "APPROVED",
      referredDateTime = ZonedDateTime.now().toString(),
      sequenceNo = "1",
      riskProfile = riskProfile,
      prisonId = "WMI",
      offenderNo = "G8105VR",
      startDate = ZonedDateTime.now().toString(),
      securityReviewedDate = ZonedDateTime.now().toString(),
      approvalDate = now().toString(),
      catType = "INITIAL",
      nomisSequenceNo = "1900-01-19",
      assessmentDate = now().toString(),
      reviewReason = now().toString(),
      dueByDate = now().toString(),
      cancelledDate = now().toString(),
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