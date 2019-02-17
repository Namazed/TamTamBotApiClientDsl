package chat.tamtam.botsdk.scopes

import chat.tamtam.botsdk.client.RequestsManager
import chat.tamtam.botsdk.model.Payload
import chat.tamtam.botsdk.state.CallbackState
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import chat.tamtam.botsdk.model.request.AnswerCallback as RequestAnswerCallback
import chat.tamtam.botsdk.model.request.SendMessage as RequestSendMessage

@BotMarker
class CallbacksScope(
    override val requests: RequestsManager,
    private val log: Logger = LoggerFactory.getLogger(CallbacksScope::class.java.name),
    private var defaultAnswer: suspend (CallbackState) -> Unit = {},
    private val callbacksAnswers: MutableMap<String, suspend (CallbackState) -> Unit> = mutableMapOf()
) : Scope {

    internal operator fun get(payloadButton: Payload) = callbacksAnswers[payloadButton.value] ?: defaultAnswer

    fun defaultAnswer(defaultAnswer: suspend (callbackState: CallbackState) -> Unit) {
        log.info("defaultAnswer: save")
        this.defaultAnswer = defaultAnswer
    }

    fun answerOnCallback(payloadButton: Payload, answer: suspend (callbackState: CallbackState) -> Unit) {
        log.info("answerOnCallback: save with payload ${payloadButton.value}")
        callbacksAnswers[payloadButton.value] = answer
    }
}
