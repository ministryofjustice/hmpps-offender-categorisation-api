package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.risk

import org.assertj.core.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonerAlertsApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.config.ResourceTest
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentDto
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentResponseDto.Companion.INCIDENT_RESPONSE_ANSWER_YES
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentResponseDto.Companion.INCIDENT_RESPONSE_QUESTION_SEXUAL_ASSAULT
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.prisonerAlert.PrisonerAlertResponseDto
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.prisonerAlert.PrisonerAlertResponseDto.Companion.ALERT_CODE_ESCAPE_LIST
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.prisonerAlert.PrisonerAlertResponseDto.Companion.ALERT_CODE_ESCAPE_LIST_HEIGHTENED
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.prisonerAlert.PrisonerAlertResponseDto.Companion.ALERT_CODE_ESCAPE_RISK
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.prisonerAlert.PrisonerAlertResponseDto.Companion.ALERT_CODE_OCGM
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.dto.incidents.TestIncidentDtoFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.dto.incidents.TestIncidentResponseDtoFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.dto.prisonerAlert.TestPrisonerAlertCodeSummaryDtoFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.dto.prisonerAlert.TestPrisonerAlertResponseDtoFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.risk.TestEscapeAlertFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.risk.EscapeAlert
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.stream.Stream

class PrisonerRiskCalculatorTest : ResourceTest() {
  private val mockPrisonerAlertsApiClient = mock<PrisonerAlertsApiClient>()
  private val mockPrisonApiClient = mock<PrisonApiClient>()
  private val frozenDateTime = "2025-01-01T10:40:34Z"
  private val fixedClock = Clock.fixed(Instant.parse(frozenDateTime), ZoneId.of("UTC"))
  private val prisonerRiskCalculator = PrisonerRiskCalculator(
    mockPrisonerAlertsApiClient,
    mockPrisonApiClient,
    fixedClock,
  )

  val alertCodes = listOf(
    ALERT_CODE_ESCAPE_RISK,
    ALERT_CODE_ESCAPE_LIST,
    ALERT_CODE_ESCAPE_LIST_HEIGHTENED,
    ALERT_CODE_OCGM,
  )

  @ParameterizedTest(name = "given alerts \"{0}\", incidents \"{1}\" and viper response \"{2}\", then it should calculate risk with escape risk alerts {3}, escape list alerts {4}, risk due to soc {5} and risk due to violence {6}")
  @MethodSource("calculateRiskArguments")
  fun calculateRisk(
    alerts: List<PrisonerAlertResponseDto>,
    incidents: List<IncidentDto>,
    expectedEscapeRiskAlerts: List<EscapeAlert>,
    expectedEscapeListAlerts: List<EscapeAlert>,
    expectedRiskDueToSoc: Boolean,
    expectedRiskDueToViolence: Boolean,
  ) {
    whenever(
      mockPrisonerAlertsApiClient.findPrisonerAlerts(
        TEST_PRISONER_NUMBER,
        alertCodes,
      ),
    ).thenReturn(alerts)
    whenever(mockPrisonApiClient.getAssaultIncidents(TEST_PRISONER_NUMBER)).thenReturn(incidents)

    val riskProfile = prisonerRiskCalculator.calculateRisk(TEST_PRISONER_NUMBER)
    Assertions.assertThat(riskProfile.escapeRiskAlerts).isEqualTo(expectedEscapeRiskAlerts)
    Assertions.assertThat(riskProfile.escapeListAlerts).isEqualTo(expectedEscapeListAlerts)
    Assertions.assertThat(riskProfile.riskDueToSeriousOrganisedCrime).isEqualTo(expectedRiskDueToSoc)
    Assertions.assertThat(riskProfile.riskDueToViolence).isEqualTo(expectedRiskDueToViolence)
  }

  companion object {
    private const val TEST_PRISONER_NUMBER = "ABC123"
    private const val ALERT_CREATED_AT_DATE = "2024-12-15"

    @JvmStatic
    fun calculateRiskArguments(): Stream<Arguments> = Stream.of(
      // active non-expired escape risk and escape list alerts
      Arguments.of(
        listOf(
          (TestPrisonerAlertResponseDtoFactory())
            .withActive(true)
            .withActiveTo(LocalDate.parse("2025-02-01"))
            .withActiveFrom(LocalDate.parse("2024-12-01"))
            .withAlertCodeSummary((TestPrisonerAlertCodeSummaryDtoFactory()).withAlertCode(ALERT_CODE_ESCAPE_RISK).build())
            .withCreatedAt(LocalDate.parse(ALERT_CREATED_AT_DATE))
            .build(),
          (TestPrisonerAlertResponseDtoFactory())
            .withActive(true)
            .withActiveTo(LocalDate.parse("2025-02-01"))
            .withActiveFrom(LocalDate.parse("2024-12-01"))
            .withAlertCodeSummary((TestPrisonerAlertCodeSummaryDtoFactory()).withAlertCode(ALERT_CODE_ESCAPE_LIST).build())
            .withCreatedAt(LocalDate.parse(ALERT_CREATED_AT_DATE))
            .build(),
        ),
        emptyList<IncidentDto>(),
        listOf(
          TestEscapeAlertFactory()
            .withActive(true)
            .withExpired(false)
            .withDateCreated(ALERT_CREATED_AT_DATE)
            .build(),
        ),
        listOf(
          TestEscapeAlertFactory()
            .withActive(true)
            .withExpired(false)
            .withDateCreated(ALERT_CREATED_AT_DATE)
            .build(),
        ),
        false,
        false,
      ),
      // inactive non-expired escape risk and escape list alerts
      Arguments.of(
        listOf(
          (TestPrisonerAlertResponseDtoFactory())
            .withActive(false)
            .withActiveTo(LocalDate.parse("2025-02-01"))
            .withActiveFrom(LocalDate.parse("2024-12-01"))
            .withAlertCodeSummary((TestPrisonerAlertCodeSummaryDtoFactory()).withAlertCode(ALERT_CODE_ESCAPE_RISK).build())
            .withCreatedAt(LocalDate.parse(ALERT_CREATED_AT_DATE))
            .build(),
          (TestPrisonerAlertResponseDtoFactory())
            .withActive(false)
            .withActiveTo(LocalDate.parse("2025-02-01"))
            .withActiveFrom(LocalDate.parse("2024-12-01"))
            .withAlertCodeSummary((TestPrisonerAlertCodeSummaryDtoFactory()).withAlertCode(ALERT_CODE_ESCAPE_LIST).build())
            .withCreatedAt(LocalDate.parse(ALERT_CREATED_AT_DATE))
            .build(),
        ),
        emptyList<IncidentDto>(),
        emptyList<EscapeAlert>(),
        emptyList<EscapeAlert>(),
        false,
        false,
      ),
      // active expired escape risk and escape list alerts
      Arguments.of(
        listOf(
          (TestPrisonerAlertResponseDtoFactory())
            .withActive(true)
            .withActiveTo(LocalDate.parse("2024-12-30"))
            .withActiveFrom(LocalDate.parse("2024-12-01"))
            .withAlertCodeSummary((TestPrisonerAlertCodeSummaryDtoFactory()).withAlertCode(ALERT_CODE_ESCAPE_RISK).build())
            .withCreatedAt(LocalDate.parse(ALERT_CREATED_AT_DATE))
            .build(),
          (TestPrisonerAlertResponseDtoFactory())
            .withActive(true)
            .withActiveTo(LocalDate.parse("2024-12-30"))
            .withActiveFrom(LocalDate.parse("2024-12-01"))
            .withAlertCodeSummary((TestPrisonerAlertCodeSummaryDtoFactory()).withAlertCode(ALERT_CODE_ESCAPE_LIST).build())
            .withCreatedAt(LocalDate.parse(ALERT_CREATED_AT_DATE))
            .build(),
        ),
        emptyList<IncidentDto>(),
        emptyList<EscapeAlert>(),
        emptyList<EscapeAlert>(),
        false,
        false,
      ),
      // active non-expired OCGM alert
      Arguments.of(
        listOf(
          (TestPrisonerAlertResponseDtoFactory())
            .withActive(true)
            .withActiveTo(LocalDate.parse("2025-05-12"))
            .withActiveFrom(LocalDate.parse("2024-12-01"))
            .withAlertCodeSummary((TestPrisonerAlertCodeSummaryDtoFactory()).withAlertCode(ALERT_CODE_OCGM).build())
            .withCreatedAt(LocalDate.parse(ALERT_CREATED_AT_DATE))
            .build(),
        ),
        emptyList<IncidentDto>(),
        emptyList<EscapeAlert>(),
        emptyList<EscapeAlert>(),
        true,
        false,
      ),
      // inactive non-expired OCGM alert
      Arguments.of(
        listOf(
          (TestPrisonerAlertResponseDtoFactory())
            .withActive(false)
            .withActiveTo(LocalDate.parse("2025-05-12"))
            .withActiveFrom(LocalDate.parse("2024-12-01"))
            .withAlertCodeSummary((TestPrisonerAlertCodeSummaryDtoFactory()).withAlertCode(ALERT_CODE_OCGM).build())
            .withCreatedAt(LocalDate.parse(ALERT_CREATED_AT_DATE))
            .build(),
        ),
        emptyList<IncidentDto>(),
        emptyList<EscapeAlert>(),
        emptyList<EscapeAlert>(),
        false,
        false,
      ),
      // active expired OCGM alert
      Arguments.of(
        listOf(
          (TestPrisonerAlertResponseDtoFactory())
            .withActive(false)
            .withActiveTo(LocalDate.parse("2024-12-30"))
            .withActiveFrom(LocalDate.parse("2024-12-01"))
            .withAlertCodeSummary((TestPrisonerAlertCodeSummaryDtoFactory()).withAlertCode(ALERT_CODE_OCGM).build())
            .withCreatedAt(LocalDate.parse(ALERT_CREATED_AT_DATE))
            .build(),
        ),
        emptyList<IncidentDto>(),
        emptyList<EscapeAlert>(),
        emptyList<EscapeAlert>(),
        false,
        false,
      ),
      // one serious assault in last 6 months
      Arguments.of(
        emptyList<PrisonerAlertResponseDto>(),
        listOf(
          TestIncidentDtoFactory()
            .withReportTime("2024-12-15T10:00:00")
            .withIncidentStatus("SOMETHING")
            .withResponses(
              listOf(
                TestIncidentResponseDtoFactory()
                  .withQuestion(INCIDENT_RESPONSE_QUESTION_SEXUAL_ASSAULT)
                  .withAnswer(INCIDENT_RESPONSE_ANSWER_YES)
                  .build(),
              ),
            )
            .build(),
        ),
        emptyList<EscapeAlert>(),
        emptyList<EscapeAlert>(),
        false,
        true,
      ),
      // one serious assault more than 6 months ago
      Arguments.of(
        emptyList<PrisonerAlertResponseDto>(),
        listOf(
          TestIncidentDtoFactory()
            .withReportTime("2023-12-30T10:00:00")
            .withIncidentStatus("SOMETHING")
            .withResponses(
              listOf(
                TestIncidentResponseDtoFactory()
                  .withQuestion(INCIDENT_RESPONSE_QUESTION_SEXUAL_ASSAULT)
                  .withAnswer(INCIDENT_RESPONSE_ANSWER_YES)
                  .build(),
              ),
            )
            .build(),
        ),
        emptyList<EscapeAlert>(),
        emptyList<EscapeAlert>(),
        false,
        false,
      ),
    )
  }
}
