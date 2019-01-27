package chat.tamtam.botsdk.scopes

import chat.tamtam.botsdk.model.Payload
import chat.tamtam.botsdk.model.response.Callback
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@BotMarker
class CallbacksScope(
    private val log: Logger = LoggerFactory.getLogger(CallbacksScope::class.java.name),
    private var defaultAnswer: suspend (Callback) -> Unit = {},
    private val callbacksAnswers: MutableMap<String, suspend (Callback) -> Unit> = mutableMapOf()
) {

    operator fun get(payloadButton: Payload) = callbacksAnswers[payloadButton.value] ?: defaultAnswer

    fun defaultAnswer(defaultAnswer: suspend (callback: Callback) -> Unit) {
        log.info("defaultAnswer: save")
        this.defaultAnswer = defaultAnswer
    }

    fun answerOnCallback(payloadButton: Payload, answer: suspend (callback: Callback) -> Unit) {
        log.info("answerOnCallback: save with payload ${payloadButton.value}")
        callbacksAnswers[payloadButton.value] = answer
    }
}