package chat.tamtam.botsdk.scopes

import chat.tamtam.botsdk.client.RequestsManager
import chat.tamtam.botsdk.model.Payload
import chat.tamtam.botsdk.state.CallbackState
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import chat.tamtam.botsdk.model.request.AnswerCallback as RequestAnswerCallback
import chat.tamtam.botsdk.model.request.SendMessage as RequestSendMessage

@BotMarker
class CallbacksScope internal constructor(
    override val requests: RequestsManager,
    private val log: Logger = LoggerFactory.getLogger(CallbacksScope::class.java.name),
    private var defaultAnswer: suspend (CallbackState) -> Unit = {},
    private val callbacksAnswers: MutableMap<String, suspend (CallbackState) -> Unit> = mutableMapOf()
) : Scope {

    internal operator fun get(payloadButton: Payload) = callbacksAnswers[payloadButton.value] ?: defaultAnswer

    /**
     * This method save action which call when [chat.tamtam.botsdk.UpdatesHandler] process new Callback which you don't know.
     *
     * @param defaultAnswer - all actions in this lambda is async.
     */
    fun defaultAnswer(defaultAnswer: suspend (callbackState: CallbackState) -> Unit) {
        log.info("defaultAnswer: save")
        this.defaultAnswer = defaultAnswer
    }

    /**
     * This method save action which call when [chat.tamtam.botsdk.UpdatesHandler] process new Callback with specific [Payload].
     *
     * @param payloadButton - this is inline class [Payload] which contains payload of specific button in [chat.tamtam.botsdk.model.request.InlineKeyboard].
     * @param answer - all actions in this lambda is async.
     */
    fun answerOnCallback(payloadButton: Payload, answer: suspend (callbackState: CallbackState) -> Unit) {
        log.info("answerOnCallback: save with payload ${payloadButton.value}")
        callbacksAnswers[payloadButton.value] = answer
    }
}
