package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.risk

import org.assertj.core.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.IncidentApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.client.PrisonerAlertsApiClient
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.config.ResourceTest
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentDto
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentReport
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentResponseDto.Companion.INCIDENT_RESPONSE_ANSWER_YES
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentResponseDto.Companion.INCIDENT_RESPONSE_QUESTION_CONCUSSION
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentResponseDto.Companion.INCIDENT_RESPONSE_QUESTION_RESULT_IN_HOSPITAL
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentResponseDto.Companion.INCIDENT_RESPONSE_QUESTION_SERIOUS_INJURY
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.IncidentResponseDto.Companion.INCIDENT_RESPONSE_QUESTION_SEXUAL_ASSAULT
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.PrisonerInvolvement
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.Question
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.incidents.Response
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.prisonerAlert.PrisonerAlertResponseDto
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.prisonerAlert.PrisonerAlertResponseDto.Companion.ALERT_CODE_ESCAPE_LIST
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.prisonerAlert.PrisonerAlertResponseDto.Companion.ALERT_CODE_ESCAPE_LIST_HEIGHTENED
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.prisonerAlert.PrisonerAlertResponseDto.Companion.ALERT_CODE_ESCAPE_RISK
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.dto.prisonerAlert.PrisonerAlertResponseDto.Companion.ALERT_CODE_OCGM
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.dto.incidents.TestIncidentDtoFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.dto.incidents.TestIncidentResponseDtoFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.dto.prisonerAlert.TestPrisonerAlertCodeSummaryDtoFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.dto.prisonerAlert.TestPrisonerAlertResponseDtoFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.response.TestViperResponseFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.factories.risk.TestEscapeAlertFactory
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.response.risk.ViperResponse
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.risk.EscapeAlert
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.UUID
import java.util.stream.Stream

class PrisonerRiskCalculatorTest : ResourceTest() {
  private val mockPrisonerAlertsApiClient = mock<PrisonerAlertsApiClient>()
  private val mockIncidentApiClient = mock<IncidentApiClient>()
  private val mockViperService = mock<ViperService>()
  private val frozenDateTime = "2025-01-01T10:40:34Z"
  private val fixedClock = Clock.fixed(Instant.parse(frozenDateTime), ZoneId.of("UTC"))
  private val prisonerRiskCalculator = PrisonerRiskCalculator(
    mockPrisonerAlertsApiClient,
    mockIncidentApiClient,
    mockViperService,
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
    numberOfIncidents: Long,
    incidents: List<IncidentDto>,
    viperResponse: ViperResponse,
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
    whenever(mockIncidentApiClient.getTotalNumberOfIncidents(TEST_PRISONER_NUMBER)).thenReturn(numberOfIncidents)
    val mapOfIncidents = incidents.associateBy { UUID.randomUUID() }
    if (mapOfIncidents.isNotEmpty()) {
      whenever(mockIncidentApiClient.getIncidentIds(TEST_PRISONER_NUMBER, 6, numberOfIncidents)).thenReturn(mapOfIncidents.keys.toList())
      mapOfIncidents.forEach { (id, dto) ->
        val generatedIncidentData = IncidentReport(
          id = id,
          type = dto.incidentType,
          status = dto.incidentStatus,
          questions = dto.responses.map { q ->
            Question(
              code = q.question,
              question = q.question,
              responses = listOf(Response(code = q.answer, response = q.answer)),
            )
          },
          prisonersInvolved = listOf(
            PrisonerInvolvement(
              prisonerNumber = TEST_PRISONER_NUMBER,
              prisonerRole = "ACTIVE_INVOLVEMENT",
            ),
          ),
        )
        whenever(mockIncidentApiClient.getDetailedIncidentReport(id)).thenReturn(
          generatedIncidentData,
        )
      }
    }

    whenever(mockViperService.getViperData(TEST_PRISONER_NUMBER)).thenReturn(viperResponse)

    val riskProfile = prisonerRiskCalculator.calculateRisk(TEST_PRISONER_NUMBER)
    Assertions.assertThat(riskProfile.escapeRiskAlerts).isEqualTo(expectedEscapeRiskAlerts)
    Assertions.assertThat(riskProfile.escapeListAlerts).isEqualTo(expectedEscapeListAlerts)
    Assertions.assertThat(riskProfile.riskDueToSeriousOrganisedCrime).isEqualTo(expectedRiskDueToSoc)
    Assertions.assertThat(riskProfile.riskDueToViolence).isEqualTo(expectedRiskDueToViolence)
  }

  companion object {
    private const val TEST_PRISONER_NUMBER = "ABC123"
    private const val ALERT_CREATED_AT_DATE = "2024-12-15"

    private val falseViperResponse = TestViperResponseFactory()
      .withAboveThreshold(false)
      .withPrisonerNumber(TEST_PRISONER_NUMBER)
      .build()

    private val trueViperResponse = TestViperResponseFactory()
      .withAboveThreshold(true)
      .withPrisonerNumber(TEST_PRISONER_NUMBER)
      .build()

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
        0L,
        emptyList<IncidentDto>(),
        falseViperResponse,
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
        0L,
        emptyList<IncidentDto>(),
        falseViperResponse,
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
        0L,
        emptyList<IncidentDto>(),
        falseViperResponse,
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
        0L,
        emptyList<IncidentDto>(),
        falseViperResponse,
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
        0L,
        emptyList<IncidentDto>(),
        falseViperResponse,
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
        0L,
        emptyList<IncidentDto>(),
        falseViperResponse,
        emptyList<EscapeAlert>(),
        emptyList<EscapeAlert>(),
        false,
        false,
      ),
      // 5 assaults total including one serious assault in last 6 months
      Arguments.of(
        emptyList<PrisonerAlertResponseDto>(),
        5L,
        listOf(
          TestIncidentDtoFactory()
            .withIncidentStatus("SOMETHING")
            .build(),
          TestIncidentDtoFactory()
            .withIncidentStatus("SOMETHING")
            .build(),
          TestIncidentDtoFactory()
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
        trueViperResponse,
        emptyList<EscapeAlert>(),
        emptyList<EscapeAlert>(),
        false,
        true,
      ),
      // 5 assaults including one serious but one is a duplicate
      Arguments.of(
        emptyList<PrisonerAlertResponseDto>(),
        4L,
        listOf(
          TestIncidentDtoFactory()
            .withIncidentStatus("SOMETHING")
            .build(),
          TestIncidentDtoFactory()
            .withIncidentStatus("SOMETHING")
            .build(),
          TestIncidentDtoFactory()
            .withIncidentStatus("SOMETHING")
            .build(),
          TestIncidentDtoFactory()
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
        trueViperResponse,
        emptyList<EscapeAlert>(),
        emptyList<EscapeAlert>(),
        false,
        false,
      ),
      // 5 assaults total including one serious assault that is more than 6 months ago
      Arguments.of(
        emptyList<PrisonerAlertResponseDto>(),
        5L,
        listOf(
          TestIncidentDtoFactory()
            .withIncidentStatus("SOMETHING")
            .build(),
          TestIncidentDtoFactory()
            .withIncidentStatus("SOMETHING")
            .build(),
          TestIncidentDtoFactory()
            .withIncidentStatus("SOMETHING")
            .build(),
          TestIncidentDtoFactory()
            .withIncidentStatus("SOMETHING")
            .build(),
        ),
        trueViperResponse,
        emptyList<EscapeAlert>(),
        emptyList<EscapeAlert>(),
        false,
        false,
      ),
      // more than 5 assaults with one serious but viper is below threshold
      Arguments.of(
        emptyList<PrisonerAlertResponseDto>(),
        5L,
        listOf(
          TestIncidentDtoFactory()
            .withIncidentStatus("SOMETHING")
            .build(),
          TestIncidentDtoFactory()
            .withIncidentStatus("SOMETHING")
            .build(),
          TestIncidentDtoFactory()
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
        falseViperResponse,
        emptyList<EscapeAlert>(),
        emptyList<EscapeAlert>(),
        false,
        false,
      ),
      // 5 assaults total including one serious assault (concussion) in last 6 months
      Arguments.of(
        emptyList<PrisonerAlertResponseDto>(),
        5L,
        listOf(
          TestIncidentDtoFactory().withIncidentStatus("SOMETHING").build(),
          TestIncidentDtoFactory().withIncidentStatus("SOMETHING").build(),
          TestIncidentDtoFactory()
            .withIncidentStatus("SOMETHING")
            .withResponses(
              listOf(
                TestIncidentResponseDtoFactory()
                  .withQuestion(INCIDENT_RESPONSE_QUESTION_CONCUSSION)
                  .withAnswer(INCIDENT_RESPONSE_ANSWER_YES)
                  .build(),
              ),
            )
            .build(),
        ),
        trueViperResponse,
        emptyList<EscapeAlert>(),
        emptyList<EscapeAlert>(),
        false,
        true,
      ),
      // 5 assaults total including one serious assault (serious injury) in last 6 months
      Arguments.of(
        emptyList<PrisonerAlertResponseDto>(),
        5L,
        listOf(
          TestIncidentDtoFactory().withIncidentStatus("SOMETHING").build(),
          TestIncidentDtoFactory().withIncidentStatus("SOMETHING").build(),
          TestIncidentDtoFactory()
            .withIncidentStatus("SOMETHING")
            .withResponses(
              listOf(
                TestIncidentResponseDtoFactory()
                  .withQuestion(INCIDENT_RESPONSE_QUESTION_SERIOUS_INJURY)
                  .withAnswer(INCIDENT_RESPONSE_ANSWER_YES)
                  .build(),
              ),
            )
            .build(),
        ),
        trueViperResponse,
        emptyList<EscapeAlert>(),
        emptyList<EscapeAlert>(),
        false,
        true,
      ),
      // 5 assaults total including one serious assault (resulted in hospital) in last 6 months
      Arguments.of(
        emptyList<PrisonerAlertResponseDto>(),
        5L,
        listOf(
          TestIncidentDtoFactory().withIncidentStatus("SOMETHING").build(),
          TestIncidentDtoFactory().withIncidentStatus("SOMETHING").build(),
          TestIncidentDtoFactory()
            .withIncidentStatus("SOMETHING")
            .withResponses(
              listOf(
                TestIncidentResponseDtoFactory()
                  .withQuestion(INCIDENT_RESPONSE_QUESTION_RESULT_IN_HOSPITAL)
                  .withAnswer(INCIDENT_RESPONSE_ANSWER_YES)
                  .build(),
              ),
            )
            .build(),
        ),
        trueViperResponse,
        emptyList<EscapeAlert>(),
        emptyList<EscapeAlert>(),
        false,
        true,
      ),
    )
  }
}
