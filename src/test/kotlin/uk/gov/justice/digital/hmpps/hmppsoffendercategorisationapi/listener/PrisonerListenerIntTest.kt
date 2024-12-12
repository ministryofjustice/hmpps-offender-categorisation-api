package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.listener

import nl.altindag.log.LogCaptor
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.kotlin.await
import org.awaitility.kotlin.untilAsserted
import org.junit.jupiter.api.Test
import org.springframework.transaction.annotation.Transactional
import software.amazon.awssdk.services.sns.model.MessageAttributeValue
import software.amazon.awssdk.services.sns.model.PublishRequest
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.integration.SqsIntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.PrisonerEventService
import uk.gov.justice.hmpps.sqs.countAllMessagesOnQueue

class PrisonerListenerIntTest : SqsIntegrationTestBase() {
  @Test
  @Transactional
  fun handleReleased() {
    val logCaptor = LogCaptor.forClass(PrisonerEventService::class.java)
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

    assertThat(logCaptor.infoLogs.contains("Handling release of")).isTrue()
    await untilAsserted {
      assertThat(prisonerListenerQueue.sqsClient.countAllMessagesOnQueue(prisonerListenerQueueUrl).get()).isEqualTo(0)
    }
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
      assertThat(prisonerListenerQueue.sqsClient.countAllMessagesOnQueue(prisonerListenerQueueUrl).get()).isEqualTo(0)
    }
  }

  private fun prisonerReleasedPayload(nomsNumber: String, eventType: String) =
    """{"eventType":"$eventType", "additionalInformation": {"nomsNumber":"$nomsNumber", "reason": "RELEASED"}}"""
}
