package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.risk

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.whenever
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonerSearchApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.TestPrisonFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.TestPrisonerFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.model.entity.PrisonerRiskProfileEntityFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.model.entity.RiskChangeEntityFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.risk.TestEscapeAlertFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.risk.TestPrisonerRiskProfileFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.RiskChangeEntity
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.risk.PrisonerRiskProfile
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.PrisonerRiskProfileRepository
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.repository.offendercategorisation.RiskChangeRepository
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.stream.Stream

class PrisonerRiskPollerTest {
  private val mockPrisonApiClient = mock<PrisonApiClient>()
  private val mockPrisonerSearchApiClient = mock<PrisonerSearchApiClient>()
  private val mockPrisonerRiskCalculator = mock<PrisonerRiskCalculator>()
  private val mockPrisonerRiskProfileRepository = mock<PrisonerRiskProfileRepository>()
  private val mockRiskChangeRepository = mock<RiskChangeRepository>()
  private val frozenDateTime = "2025-01-01T10:40:34Z"
  private val fixedClock = Clock.fixed(Instant.parse(frozenDateTime), ZoneId.of("UTC"))

  private val prisonerRiskPoller = PrisonerRiskPoller(
    mockPrisonApiClient,
    mockPrisonerSearchApiClient,
    mockPrisonerRiskCalculator,
    mockPrisonerRiskProfileRepository,
    mockRiskChangeRepository,
    fixedClock,
  )

  @ParameterizedTest(name = "given old profile \"{0}\" and new profile \"{1}\" a risk change is raised")
  @MethodSource("pollPrisonersRaisingRiskChange")
  fun `should poll prisoners risk and raise risk change`(
    oldProfile: PrisonerRiskProfile,
    newProfile: PrisonerRiskProfile,
  ) {
    mockFindPrisons()
    mockFindPrisoners("C")
    whenever(mockPrisonerRiskCalculator.calculateRisk("A1234BC")).thenReturn(newProfile)
    val testOldRiskProfileString = jacksonObjectMapper().writeValueAsString(oldProfile)
    mockGetPrisonerRiskProfile(testOldRiskProfileString)
    prisonerRiskPoller.pollPrisonersRisk()
    verifyPrisonerRiskProfileSaved(newProfile)
    verify(mockRiskChangeRepository, times(1)).save(
      argThat { entity ->
        entity.offenderNo == "A1234BC" &&
          entity.newProfile == jacksonObjectMapper().writeValueAsString(newProfile) &&
          entity.oldProfile == testOldRiskProfileString &&
          entity.raisedDate == Instant.parse(frozenDateTime).atZone(ZoneId.of("UTC")) &&
          entity.prisonId == TEST_AGENCY_ID &&
          entity.status == RiskChangeEntity.STATUS_NEW
      },
    )
  }

  @ParameterizedTest(name = "given old profile \"{0}\" and new profile \"{1}\" a risk change is not raised")
  @MethodSource("pollPrisonersNotRaisingRiskChange")
  fun `should poll prisoners risk but not raise risk change`(
    oldProfile: PrisonerRiskProfile,
    newProfile: PrisonerRiskProfile,
  ) {
    mockFindPrisons()
    mockFindPrisoners("C")
    whenever(mockPrisonerRiskCalculator.calculateRisk("A1234BC")).thenReturn(newProfile)
    val testOldRiskProfileString = jacksonObjectMapper().writeValueAsString(oldProfile)
    mockGetPrisonerRiskProfile(testOldRiskProfileString)
    prisonerRiskPoller.pollPrisonersRisk()
    verifyPrisonerRiskProfileSaved(newProfile)
    verify(mockRiskChangeRepository, never()).save(any())
  }

  @ParameterizedTest(name = "given category \"{0}\" a risk change is raised")
  @MethodSource("categoriesThatShouldRaiseRiskChange")
  fun `should poll prisoners risk and raise risk change because of category`(
    category: String,
  ) {
    val testOldProfile = TestPrisonerRiskProfileFactory()
      .withEscapeRiskAlerts(emptyList())
      .withEscapeListAlerts(emptyList())
      .withRiskDueToViolence(false)
      .withRiskDueToSeriousOrganisedCrime(false)
      .build()
    val testNewProfile = TestPrisonerRiskProfileFactory()
      .withEscapeRiskAlerts(emptyList())
      .withEscapeListAlerts(emptyList())
      .withRiskDueToSeriousOrganisedCrime(false)
      .withRiskDueToViolence(true)
      .build()

    mockFindPrisons()
    mockFindPrisoners(category)
    whenever(mockPrisonerRiskCalculator.calculateRisk("A1234BC")).thenReturn(testNewProfile)
    val testOldRiskProfileString = jacksonObjectMapper().writeValueAsString(testOldProfile)
    mockGetPrisonerRiskProfile(testOldRiskProfileString)
    prisonerRiskPoller.pollPrisonersRisk()
    verifyPrisonerRiskProfileSaved(testNewProfile)
    verify(mockRiskChangeRepository, times(1)).save(
      argThat { entity ->
        entity.offenderNo == "A1234BC" &&
          entity.newProfile == jacksonObjectMapper().writeValueAsString(testNewProfile) &&
          entity.oldProfile == testOldRiskProfileString &&
          entity.raisedDate == Instant.parse(frozenDateTime).atZone(ZoneId.of("UTC")) &&
          entity.prisonId == TEST_AGENCY_ID &&
          entity.status == RiskChangeEntity.STATUS_NEW
      },
    )
  }

  @ParameterizedTest(name = "given category \"{0}\" a risk change is not raised")
  @MethodSource("categoriesThatShouldNotRaiseRiskChange")
  fun `should poll prisoners risk but not raise risk change because of category`(
    category: String,
  ) {
    val testOldProfile = TestPrisonerRiskProfileFactory()
      .withEscapeRiskAlerts(emptyList())
      .withEscapeListAlerts(emptyList())
      .withRiskDueToViolence(false)
      .withRiskDueToSeriousOrganisedCrime(false)
      .build()
    val testNewProfile = TestPrisonerRiskProfileFactory()
      .withEscapeRiskAlerts(emptyList())
      .withEscapeListAlerts(emptyList())
      .withRiskDueToSeriousOrganisedCrime(false)
      .withRiskDueToViolence(true)
      .build()

    mockFindPrisons()
    mockFindPrisoners(category)
    whenever(mockPrisonerRiskCalculator.calculateRisk("A1234BC")).thenReturn(testNewProfile)
    val testOldRiskProfileString = jacksonObjectMapper().writeValueAsString(testOldProfile)
    mockGetPrisonerRiskProfile(testOldRiskProfileString)
    prisonerRiskPoller.pollPrisonersRisk()
    verifyPrisonerRiskProfileSaved(testNewProfile)
    verify(mockRiskChangeRepository, never()).save(any())
  }

  @Test
  fun `should poll prisoners but not raise new risk change when risk change already exists`() {
    val testOldestProfile = TestPrisonerRiskProfileFactory()
      .withEscapeRiskAlerts(emptyList())
      .withEscapeListAlerts(emptyList())
      .withRiskDueToViolence(false)
      .withRiskDueToSeriousOrganisedCrime(false)
      .build()
    val testOldestProfileString = jacksonObjectMapper().writeValueAsString(testOldestProfile)
    val testOldProfile = TestPrisonerRiskProfileFactory()
      .withEscapeRiskAlerts(emptyList())
      .withEscapeListAlerts(emptyList())
      .withRiskDueToViolence(true)
      .withRiskDueToSeriousOrganisedCrime(false)
      .build()
    val testOldRiskProfileString = jacksonObjectMapper().writeValueAsString(testOldProfile)
    val testNewProfile = TestPrisonerRiskProfileFactory()
      .withEscapeRiskAlerts(emptyList())
      .withEscapeListAlerts(emptyList())
      .withRiskDueToSeriousOrganisedCrime(true)
      .withRiskDueToViolence(true)
      .build()
    val testNewProfileString = jacksonObjectMapper().writeValueAsString(testNewProfile)

    mockFindPrisons()
    mockFindPrisoners("C")
    whenever(mockPrisonerRiskCalculator.calculateRisk("A1234BC")).thenReturn(testNewProfile)
    mockGetPrisonerRiskProfile(testOldRiskProfileString)
    val existingNewRiskChange = RiskChangeEntityFactory()
      .withOffenderNo("A1234BC")
      .withPrisonId(TEST_AGENCY_ID)
      .withOldProfile(testOldestProfileString)
      .withNewProfile(testOldRiskProfileString)
      .withStatus(RiskChangeEntity.STATUS_NEW)
      .withRaisedDate(Instant.parse("2024-06-20T09:30:00Z").atZone(ZoneId.of("UTC")))
      .build()
    whenever(mockRiskChangeRepository.findByStatusAndOffenderNo(RiskChangeEntity.STATUS_NEW, "A1234BC")).thenReturn(
      existingNewRiskChange,
    )
    prisonerRiskPoller.pollPrisonersRisk()
    verifyPrisonerRiskProfileSaved(testNewProfile)

    verify(mockRiskChangeRepository, times(1)).save(
      argThat { entity ->
        entity.offenderNo == "A1234BC" &&
          entity.newProfile == testNewProfileString &&
          entity.oldProfile == testOldestProfileString &&
          entity.raisedDate == Instant.parse(frozenDateTime).atZone(ZoneId.of("UTC")) &&
          entity.prisonId == TEST_AGENCY_ID &&
          entity.status == RiskChangeEntity.STATUS_NEW
      },
    )
  }

  private fun mockGetPrisonerRiskProfile(testOldRiskProfileString: String?) = whenever(
    mockPrisonerRiskProfileRepository
      .findByOffenderNo("A1234BC"),
  ).thenReturn(
    PrisonerRiskProfileEntityFactory()
      .withOffenderNo("A1234BC")
      .withRiskProfile(testOldRiskProfileString)
      .build(),
  )

  private fun verifyPrisonerRiskProfileSaved(newProfile: PrisonerRiskProfile) = verify(
    mockPrisonerRiskProfileRepository,
    times(1),
  ).save(
    argThat { entity ->
      entity.offenderNo == "A1234BC" &&
        entity.riskProfile == jacksonObjectMapper().writeValueAsString(newProfile) &&
        entity.calculatedAt == Instant.parse(frozenDateTime).atZone(ZoneId.of("UTC"))
    },
  )

  private fun mockFindPrisoners(category: String) = whenever(
    mockPrisonerSearchApiClient.findPrisonersByAgencyId(TEST_AGENCY_ID, 0, 100),
  ).thenReturn(
    listOf(
      TestPrisonerFactory()
        .withPrisonerNumber("A1234BC")
        .withCategory(category)
        .build(),
    ),
  )

  private fun mockFindPrisons() = whenever(
    mockPrisonApiClient.findPrisons(),
  ).thenReturn(
    listOf(
      TestPrisonFactory().withAgencyId(TEST_AGENCY_ID).build(),
    ),
  )

  companion object {
    private const val ALERT_CREATED_AT_DATE = "2024-12-15"
    private const val TEST_AGENCY_ID = "TEST"

    @JvmStatic
    fun pollPrisonersRaisingRiskChange(): Stream<Arguments> = Stream.of(
      // escape risk alert added
      Arguments.of(
        TestPrisonerRiskProfileFactory()
          .withEscapeRiskAlerts(emptyList())
          .withEscapeListAlerts(emptyList())
          .withRiskDueToViolence(false)
          .withRiskDueToSeriousOrganisedCrime(false)
          .build(),
        TestPrisonerRiskProfileFactory()
          .withEscapeRiskAlerts(
            listOf(
              TestEscapeAlertFactory()
                .withActive(true)
                .withExpired(false)
                .withDateCreated(ALERT_CREATED_AT_DATE)
                .build(),
            ),
          )
          .withEscapeListAlerts(emptyList())
          .withRiskDueToSeriousOrganisedCrime(false)
          .withRiskDueToViolence(false)
          .build(),
      ),
      // escape risk alert removed
      Arguments.of(
        TestPrisonerRiskProfileFactory()
          .withEscapeRiskAlerts(
            listOf(
              TestEscapeAlertFactory()
                .withActive(true)
                .withExpired(false)
                .withDateCreated(ALERT_CREATED_AT_DATE)
                .build(),
            ),
          )
          .withEscapeListAlerts(emptyList())
          .withRiskDueToViolence(false)
          .withRiskDueToSeriousOrganisedCrime(false)
          .build(),
        TestPrisonerRiskProfileFactory()
          .withEscapeRiskAlerts(emptyList())
          .withEscapeListAlerts(emptyList())
          .withRiskDueToSeriousOrganisedCrime(false)
          .withRiskDueToViolence(false)
          .build(),
      ),
      // escape list alert added
      Arguments.of(
        TestPrisonerRiskProfileFactory()
          .withEscapeRiskAlerts(emptyList())
          .withEscapeListAlerts(emptyList())
          .withRiskDueToViolence(false)
          .withRiskDueToSeriousOrganisedCrime(false)
          .build(),
        TestPrisonerRiskProfileFactory()
          .withEscapeRiskAlerts(emptyList())
          .withEscapeListAlerts(
            listOf(
              TestEscapeAlertFactory()
                .withActive(true)
                .withExpired(false)
                .withDateCreated(ALERT_CREATED_AT_DATE)
                .build(),
            ),
          )
          .withRiskDueToSeriousOrganisedCrime(false)
          .withRiskDueToViolence(false)
          .build(),
      ),
      // escape list alert removed
      Arguments.of(
        TestPrisonerRiskProfileFactory()
          .withEscapeRiskAlerts(emptyList())
          .withEscapeListAlerts(
            listOf(
              TestEscapeAlertFactory()
                .withActive(true)
                .withExpired(false)
                .withDateCreated(ALERT_CREATED_AT_DATE)
                .build(),
            ),
          )
          .withRiskDueToViolence(false)
          .withRiskDueToSeriousOrganisedCrime(false)
          .build(),
        TestPrisonerRiskProfileFactory()
          .withEscapeRiskAlerts(emptyList())
          .withEscapeListAlerts(emptyList())
          .withRiskDueToSeriousOrganisedCrime(false)
          .withRiskDueToViolence(false)
          .build(),
      ),
      // serious organised crime risk added
      Arguments.of(
        TestPrisonerRiskProfileFactory()
          .withEscapeRiskAlerts(emptyList())
          .withEscapeListAlerts(emptyList())
          .withRiskDueToViolence(false)
          .withRiskDueToSeriousOrganisedCrime(false)
          .build(),
        TestPrisonerRiskProfileFactory()
          .withEscapeRiskAlerts(emptyList())
          .withEscapeListAlerts(emptyList())
          .withRiskDueToSeriousOrganisedCrime(true)
          .withRiskDueToViolence(false)
          .build(),
      ),
      // violence risk added
      Arguments.of(
        TestPrisonerRiskProfileFactory()
          .withEscapeRiskAlerts(emptyList())
          .withEscapeListAlerts(emptyList())
          .withRiskDueToViolence(false)
          .withRiskDueToSeriousOrganisedCrime(false)
          .build(),
        TestPrisonerRiskProfileFactory()
          .withEscapeRiskAlerts(emptyList())
          .withEscapeListAlerts(emptyList())
          .withRiskDueToSeriousOrganisedCrime(false)
          .withRiskDueToViolence(true)
          .build(),
      ),
    )

    @JvmStatic
    fun pollPrisonersNotRaisingRiskChange(): Stream<Arguments> = Stream.of(
      // violence risk removed
      Arguments.of(
        TestPrisonerRiskProfileFactory()
          .withEscapeRiskAlerts(emptyList())
          .withEscapeListAlerts(emptyList())
          .withRiskDueToViolence(true)
          .withRiskDueToSeriousOrganisedCrime(false)
          .build(),
        TestPrisonerRiskProfileFactory()
          .withEscapeRiskAlerts(emptyList())
          .withEscapeListAlerts(emptyList())
          .withRiskDueToSeriousOrganisedCrime(false)
          .withRiskDueToViolence(false)
          .build(),
      ),
      // serious organised crime risk removed
      Arguments.of(
        TestPrisonerRiskProfileFactory()
          .withEscapeRiskAlerts(emptyList())
          .withEscapeListAlerts(emptyList())
          .withRiskDueToViolence(false)
          .withRiskDueToSeriousOrganisedCrime(true)
          .build(),
        TestPrisonerRiskProfileFactory()
          .withEscapeRiskAlerts(emptyList())
          .withEscapeListAlerts(emptyList())
          .withRiskDueToSeriousOrganisedCrime(false)
          .withRiskDueToViolence(false)
          .build(),
      ),
    )

    @JvmStatic
    fun categoriesThatShouldRaiseRiskChange(): Stream<Arguments> = Stream.of(
      Arguments.of("D"),
      Arguments.of("C"),
      Arguments.of("J"),
    )

    @JvmStatic
    fun categoriesThatShouldNotRaiseRiskChange(): Stream<Arguments> = Stream.of(
      Arguments.of("B"),
      Arguments.of("A"),
      Arguments.of("U"),
      Arguments.of("I"),
      Arguments.of("R"),
      Arguments.of("T"),
      Arguments.of("Z"),
    )
  }
}
