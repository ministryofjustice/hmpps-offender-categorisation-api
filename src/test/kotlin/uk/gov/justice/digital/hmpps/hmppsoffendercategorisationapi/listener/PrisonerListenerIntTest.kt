package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.listener

import org.assertj.core.api.Assertions.assertThat
import org.awaitility.kotlin.await
import org.awaitility.kotlin.untilAsserted
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.transaction.annotation.Transactional
import software.amazon.awssdk.services.sns.model.MessageAttributeValue
import software.amazon.awssdk.services.sns.model.PublishRequest
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.integration.SqsIntegrationTestBase
import uk.gov.justice.hmpps.sqs.countAllMessagesOnQueue

@ExtendWith(OutputCaptureExtension::class)
class PrisonerListenerIntTest : SqsIntegrationTestBase() {
  @Test
  @Transactional
  fun handleReleased(output: CapturedOutput) {
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

    await untilAsserted {
      assertThat(prisonerListenerQueue.sqsClient.countAllMessagesOnQueue(prisonerListenerQueue.queueUrl).get()).isEqualTo(0)
    }
    assertThat(output).contains("Handling release of")
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
}
