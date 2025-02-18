package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.integration

import org.awaitility.kotlin.await
import org.awaitility.kotlin.matches
import org.awaitility.kotlin.untilCallTo
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import software.amazon.awssdk.services.sqs.model.PurgeQueueRequest
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.integration.LocalStackContainer.setLocalStackProperties
import uk.gov.justice.hmpps.sqs.HmppsQueue
import uk.gov.justice.hmpps.sqs.HmppsQueueService
import uk.gov.justice.hmpps.sqs.MissingQueueException
import uk.gov.justice.hmpps.sqs.countMessagesOnQueue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SqsIntegrationTestBase : IntegrationTestBase() {

  @Autowired
  private lateinit var hmppsQueueService: HmppsQueueService

  protected val domainEventsTopic by lazy {
    hmppsQueueService.findByTopicId("domainevents")
      ?: throw MissingQueueException("HmppsTopic domainevents not found")
  }
  protected val domainEventsTopicSnsClient by lazy { domainEventsTopic.snsClient }
  protected val domainEventsTopicArn by lazy { domainEventsTopic.arn }

  protected val prisonerListenerQueue by lazy { hmppsQueueService.findByQueueId("domaineventsqueue") as HmppsQueue }

  @BeforeEach
  fun cleanQueue() {
    prisonerListenerQueue.sqsClient.purgeQueue(PurgeQueueRequest.builder().queueUrl(prisonerListenerQueue.queueUrl).build())
    await untilCallTo {
      prisonerListenerQueue.sqsClient.countMessagesOnQueue(prisonerListenerQueue.queueUrl).get()
    } matches { it == 0 }
  }

  fun getNumberOfMessagesCurrentlyOnQueue(): Int? =
    prisonerListenerQueue.sqsClient.countMessagesOnQueue(prisonerListenerQueue.queueUrl).get()

  companion object {
    private val localStackContainer = LocalStackContainer.instance

    @Suppress("unused")
    @JvmStatic
    @DynamicPropertySource
    fun testcontainers(registry: DynamicPropertyRegistry) {
      localStackContainer?.also { setLocalStackProperties(it, registry) }
    }
  }
}
