package uk.gov.justice.digital.hmpps.hmppsoffendercategorisationapi.services

import com.fasterxml.jackson.databind.ObjectMapper
import io.awspring.cloud.sqs.annotation.SqsListener
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class PrisonerOffenderSearchEventListenerService(
    private val mapper: ObjectMapper,
) {
    private companion object {
        val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @SqsListener("prisoneroffendersearch", factory = "hmppsQueueContainerFactoryProxy")
    fun processMessage(rawMessage: String) {
        val (message, messageAttributes) = mapper.readValue(rawMessage, Message::class.java)
        val eventType = messageAttributes.eventType.Value
        log.info("Received message $message, type $eventType")
    }
}

data class EventType(val Value: String, val Type: String)
data class MessageAttributes(val eventType: EventType)
data class Message(
    val Message: String,
    val MessageAttributes: MessageAttributes,
)