package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.enum.CatType
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.enum.ReviewReason
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.enum.SecurityReferralStatus
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.CatForm
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.LiteCategory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.NextReviewChangeHistory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.RiskProfile
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.SecurityReferral
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.catform.riskprofile.LifeProfile
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.common.RedactedSection
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Escape
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Profile
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.RiskChange
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.riskchange.Violence
import java.io.File
import java.io.InputStream

open class BaseSarUnitTest {
  companion object {

    val json = jacksonObjectMapper()

    fun loadTestData(filename: String): String {
      val inputStream: InputStream = File("src/test/resources/testdata/$filename").inputStream()
      return inputStream.bufferedReader().use { it.readText() }
    }

    fun loadExpectedOutput(filename: String): String {
      val inputStream: InputStream = File("src/test/resources/expectedoutput/$filename").inputStream()
      return inputStream.bufferedReader().use { it.readText() }
    }

    fun jsonStringToMap(json: String): Map<String, Any> {
      val data = ObjectMapper().readValue<MutableMap<String, Any>>(json)
      return data
    }

    @JvmStatic
    protected val violenceProfile = Violence(
      nomsId = "G8105VR",
      numberOfAssaults = 0,
      notifySafetyCustodyLead = false,
      numberOfSeriousAssaults = 0,
      provisionalCategorisation = "C",
      numberOfNonSeriousAssaults = 0,
      veryHighRiskViolentOffender = false,
    )

    @JvmStatic
    protected val riskProfilerViolence = Violence(
      nomsId = "G2194GK",
      numberOfAssaults = 0,
      notifySafetyCustodyLead = false,
      numberOfSeriousAssaults = 0,
      provisionalCategorisation = "C",
      numberOfNonSeriousAssaults = 0,
      veryHighRiskViolentOffender = false,
    )

    protected val riskProfile = RiskProfile(
      catHistory = listOf(
        mapOf(
          "bookingId" to 12345,
          "offenderNo" to "ABC123",
          "approvalDate" to "2024-02-18",
          "assessmentSequence" to 5,
          "assessmentDate" to "2024-03-23",
          "classification" to "Cat C",
          "nextReviewDate" to "2025-02-18",
          "assessmentStatus" to "A",
          "agencyDescription" to "Pentonville",
          "assessmentComment" to "something",
          "classificationCode" to "C",
          "approvalDateDisplay" to "18/01/2024",
          "cellSharingAlertFlag" to false,
          "assessmentDescription" to "something",
        ),
      ),
      history = RedactedSection(),
      offences = emptyList(),
      socProfile = null,
      lifeProfile = LifeProfile(
        life = true,
        nomsId = "G8105VR",
        provisionalCategorisation = "B",
      ),
      escapeProfile = Escape(
        nomsId = "G8105VR",
        provisionalCategorisation = "C",
      ),
      violenceProfile,
      extremismProfile = RedactedSection(),
    )

    @JvmStatic
    protected val catForm = CatForm(
      id = "1598",
      formResponse = jsonStringToMap(loadTestData("/form/form_response.json")),
      status = "APPROVED",
      referredDate = "2023-03-21 15:08:50.982 +0000",
      riskProfile = riskProfile,
      prisonId = "LEI",
      offenderNo = "G8105VR",
      startDate = "2023-03-21 15:09:00.266 +0000",
      securityReviewedDate = "2023-03-21 15:09:00.266 +0000",
      approvalDate = "2023-03-21",
      catType = CatType.INITIAL,
      assessmentDate = "2023-03-21",
      reason = ReviewReason.MANUAL,
      dueByDate = "1977-12-16",
      cancelledDate = "1977-12-17",
    )

    @JvmStatic
    protected val security = SecurityReferral(
      id = "2",
      offenderNo = "G2550VO",
      prisonId = "LEI",
      statusId = SecurityReferralStatus.REFERRED,
      raisedDate = "2019-09-19 13:33:21.123 +0100",
      processedDate = "2019-09-19 13:36:46.335 +0100",
    )

    @JvmStatic
    protected val liteCategory = LiteCategory(
      sequence = "5",
      category = "U",
      supervisorCategory = "U",
      offenderNo = "G0089UO",
      prisonId = "LPI",
      createdDate = "2020-05-18 13:58:42.435 +0100",
      approvedDate = "2020-05-18 01:00:00.000 +0100",
      assessmentCommittee = "OCA",
      assessmentComment = "Testing the creation of an unsentenced",
      nextReviewDate = "2020-11-18",
      placementPrisonId = "ASI",
      approvedCommittee = "OCA",
      approvedPlacementPrisonId = "ASI",
      approvedPlacementComment = "approved placement comment",
      approvedComment = "approval comment",
    )

    @JvmStatic
    protected val riskChange = RiskChange(
      id = "1",
      oldProfile = Profile(
        escape = Escape(
          nomsId = "G0048VL",
          provisionalCategorisation = "C",
        ),
        violence = Violence(
          nomsId = "G0048VL",
          numberOfAssaults = 0,
          provisionalCategorisation = "C",
          veryHighRiskViolentOffender = false,
        ),
      ),
      newProfile = Profile(
        soc = null,
        escape = Escape(
          nomsId = "G0048VL",
          provisionalCategorisation = "C",
        ),
        violence = Violence(
          nomsId = "G0048VL",
          numberOfAssaults = 0,
          provisionalCategorisation = "C",
          veryHighRiskViolentOffender = false,
        ),
        extremism = RedactedSection(),
      ),
      offenderNo = "",
      prisonId = "BAI",
      status = "REVIEWED_FIRST",
      raisedDate = "2019-09-18 10:45:34.166 +0100",
    )

    @JvmStatic
    protected val nextReviewChangeHistory = NextReviewChangeHistory(
      id = "1",
      offenderNo = "G7919UD",
      nextReviewDate = "2021-12-12",
      reason = "testing",
      changeDate = "2021-09-22 10:25:44.395 +0100",
    )
  }
}
