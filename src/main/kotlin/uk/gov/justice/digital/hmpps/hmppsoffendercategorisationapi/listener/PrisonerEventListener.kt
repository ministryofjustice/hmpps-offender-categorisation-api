package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.listener

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.awspring.cloud.sqs.annotation.SqsListener
import io.opentelemetry.api.trace.SpanKind
import io.opentelemetry.instrumentation.annotations.WithSpan
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services.PrisonerEventService

@Service
class PrisonerEventListener(
  private val objectMapper: ObjectMapper,
  private val prisonerEventService: PrisonerEventService,
) {

  private companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  @SqsListener("domainevents", factory = "hmppsQueueContainerFactoryProxy")
  @WithSpan(value = "dps-core-oc_api_queue_for_domain_events", kind = SpanKind.SERVER)
  fun onDomainEvent(
    rawMessage: String,
  ) {
    log.debug("Received message {}", rawMessage)
    val sqsMessage: SQSMessage = objectMapper.readValue(rawMessage)
    return when (sqsMessage.Type) {
      "Notification" -> {
        val (eventType) = objectMapper.readValue<HMPPSDomainEvent>(sqsMessage.Message)
        processMessage(eventType, sqsMessage.Message)
      } else -> {}
    }
  }

  private fun processMessage(eventType: String, message: String) {
    when (eventType) {
      "prisoner-offender-search.prisoner.released",
      ->
        prisonerEventService.handleRelease(objectMapper.readValue(message))

      else -> log.info("Received a message I wasn't expecting: {}", eventType)
    }
  }
}
