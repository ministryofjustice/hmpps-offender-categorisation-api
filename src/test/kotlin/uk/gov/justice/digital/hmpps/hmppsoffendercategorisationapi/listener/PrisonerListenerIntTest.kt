package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.listener

import org.assertj.core.api.Assertions.assertThat
import org.awaitility.kotlin.await
import org.awaitility.kotlin.matches
import org.awaitility.kotlin.untilAsserted
import org.awaitility.kotlin.untilCallTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import software.amazon.awssdk.services.sns.model.MessageAttributeValue
import software.amazon.awssdk.services.sns.model.PublishRequest
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.integration.SqsIntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.entity.offendercategorisation.FormEntity
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.enum.CatType
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.model.enum.ReviewReason
import uk.gov.justice.hmpps.sqs.countAllMessagesOnQueue

@ExtendWith(OutputCaptureExtension::class)
@Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class PrisonerListenerIntTest : SqsIntegrationTestBase() {

  @Autowired
  private lateinit var jdbcTemplate: JdbcTemplate

  @ParameterizedTest
  @MethodSource("statusesWhichShouldBeUpdated")
  @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = ["classpath:repository/reset.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @Transactional
  fun handleReleased(status: String) {
    val testForm = "{\"something\": \"else\"}"
    val testUsername = "TEST_GEN"
    val testDateTime = "2025-01-25 11:24:12.768"
    val testRiskProfile = "{\"risk\": \"profile\"}"
    val testPrisonId = "BMI"
    val testOffenderNo = "ABC123"
    val testDate = "2025-01-20"

    jdbcTemplate.execute(
      "INSERT INTO form (form_response,booking_id,user_id,status,assigned_user_id,referred_date,referred_by,sequence_no,risk_profile,prison_id,offender_no,start_date,security_reviewed_by,cat_type,nomis_sequence_no,assessment_date,approved_by,assessed_by,review_reason,due_by_date,cancelled_by) " +
        "VALUES ('$testForm',0,'$testUsername','$status','$testUsername','$testDateTime','',1,'$testRiskProfile','$testPrisonId','$testOffenderNo','$testDateTime','','RECAT',1,'$testDateTime','','','MANUAL','$testDate','')",
    )

    val eventType = "prisoner-offender-search.prisoner.released"
    domainEventsTopicSnsClient.publish(
      PublishRequest.builder().topicArn(domainEventsTopicArn)
        .message(prisonerReleasedPayload("BCDEFG", eventType))
        .messageAttributes(
          mapOf(
            "eventType" to MessageAttributeValue.builder().dataType("String")
              .stringValue(eventType).build(),
          ),
        ).build(),
    ).get()

    await untilCallTo {
      prisonerListenerQueue.sqsClient.countAllMessagesOnQueue(prisonerListenerQueue.queueUrl).get()
    } matches { it == 0 }

    val formEntities = jdbcTemplate.query(
      "SELECT * FROM public.form",
    ) { rs, _ ->
      FormEntity(
        formResponse = rs.getString("form_response"),
        bookingId = rs.getLong("booking_id"),
        userId = rs.getString("user_id"),
        status = rs.getString("status"),
        sequenceNo = rs.getInt("sequence_no"),
        referredDate = rs.getTimestamp("referred_date")?.toLocalDateTime()!!,
        riskProfile = rs.getString("risk_profile"),
        prisonId = rs.getString("prison_id"),
        offenderNo = rs.getString("offender_no"),
        startDate = rs.getTimestamp("start_date")?.toLocalDateTime()!!,
        securityReviewedBy = rs.getString("security_reviewed_by"),
        catType = CatType.valueOf(rs.getString("cat_type")),
        nomisSequenceNo = rs.getInt("nomis_sequence_no"),
        reviewReason = ReviewReason.valueOf(rs.getString("review_reason")),
        dueByDate = rs.getTimestamp("due_by_date")?.toLocalDateTime()?.toLocalDate(),
        cancelledDate = rs.getTimestamp("cancelled_date")?.toLocalDateTime(),
        cancelledBy = rs.getString("cancelled_by"),
        approvalDate = null,
        securityReviewedDate = null,
        assessmentDate = null,
      )
    }
    println(formEntities)
    assertThat(formEntities.count()).isEqualTo(1)
    assertThat(formEntities[0].getStatus()).isEqualTo(FormEntity.STATUS_CANCELLED_AFTER_RELEASE)
  }

  @Test
  @Transactional
  fun handleOtherEvent(output: CapturedOutput) {
    val eventType = "prisoner-offender-search.prisoner.something"
    domainEventsTopicSnsClient.publish(
      PublishRequest.builder().topicArn(domainEventsTopicArn)
        .message(prisonerReleasedPayload("BCDEFG", eventType))
        .messageAttributes(
          mapOf(
            "eventType" to MessageAttributeValue.builder().dataType("String")
              .stringValue(eventType).build(),
          ),
        ).build(),
    ).get()

    await untilAsserted {
      assertThat(prisonerListenerQueue.sqsClient.countAllMessagesOnQueue(prisonerListenerQueue.queueUrl).get()).isEqualTo(0)
    }
    assertThat(output).doesNotContain("Handling release of")
  }

  @Test
  @Transactional
  fun shouldFailForNonReleasedType() {
    val eventType = "prisoner-offender-search.prisoner.error"
    domainEventsTopicSnsClient.publish(
      PublishRequest.builder().topicArn(domainEventsTopicArn)
        .message(prisonerReleasedPayload("BCDEFG", eventType))
        .messageAttributes(
          mapOf(
            "eventType" to MessageAttributeValue.builder().dataType("String")
              .stringValue(eventType).build(),
          ),
        ).build(),
    ).get()

    await untilAsserted {
      assertThat(prisonerListenerQueue.sqsClient.countAllMessagesOnQueue(prisonerListenerQueue.queueUrl).get()).isEqualTo(0)
    }
  }

  private fun prisonerReleasedPayload(nomsNumber: String, eventType: String) = """{"eventType":"$eventType", "additionalInformation": {"nomsNumber":"$nomsNumber", "reason": "RELEASED"}}"""

  companion object {
    @JvmStatic
    fun statusesWhichShouldBeUpdated(): List<String> = listOf(
      FormEntity.STATUS_STARTED,
      FormEntity.STATUS_SECURITY_BACK,
      FormEntity.STATUS_SECURITY_AUTO,
      FormEntity.STATUS_SECURITY_MANUAL,
      FormEntity.STATUS_AWAITING_APPROVAL,
      FormEntity.STATUS_SECURITY_FLAGGED,
      FormEntity.STATUS_SUPERVISOR_BACK,
    )
  }
}
