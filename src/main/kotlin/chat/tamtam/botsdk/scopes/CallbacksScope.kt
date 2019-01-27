package chat.tamtam.botsdk.scopes

import chat.tamtam.botsdk.client.BotHttpManager
import chat.tamtam.botsdk.model.Payload
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.request.AttachType
import chat.tamtam.botsdk.model.request.AttachmentKeyboard
import chat.tamtam.botsdk.model.request.InlineKeyboard
import chat.tamtam.botsdk.model.request.SendMessage
import chat.tamtam.botsdk.model.response.Callback
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@BotMarker
class CallbacksScope(
    private val botHttpManager: BotHttpManager,
    private val log: Logger = LoggerFactory.getLogger(CallbacksScope::class.java.name),
    private var defaultAnswer: suspend (Callback) -> Unit = {},
    private val callbacksAnswers: MutableMap<String, suspend (Callback) -> Unit> = mutableMapOf()
) {

    internal operator fun get(payloadButton: Payload) = callbacksAnswers[payloadButton.value] ?: defaultAnswer

    fun defaultAnswer(defaultAnswer: suspend (callback: Callback) -> Unit) {
        log.info("defaultAnswer: save")
        this.defaultAnswer = defaultAnswer
    }

    fun answerOnCallback(payloadButton: Payload, answer: suspend (callback: Callback) -> Unit) {
        log.info("answerOnCallback: save with payload ${payloadButton.value}")
        callbacksAnswers[payloadButton.value] = answer
    }

    private suspend fun send(userId: UserId, sendMessage: SendMessage) {
        botHttpManager.sendMessage(userId, sendMessage)
    }

    suspend infix fun Callback.send(sendMessage: SendMessage) {
        botHttpManager.sendMessage(UserId(this.user.userId), sendMessage)
    }

    infix fun Callback.createText(text: String) : Pair<UserId, SendMessage> {
        return Pair(UserId(user.userId), SendMessage(text))
    }

    suspend infix fun Pair<UserId, SendMessage>.sendWith(keyboard: InlineKeyboard) {
        val attaches = listOf(AttachmentKeyboard(AttachType.INLINE_KEYBOARD.value.toLowerCase(), keyboard))
        send(first, SendMessage(second.text, attaches, second.notifyUser))
    }

    suspend infix fun Callback.sendText(text: String) {
        send(SendMessage(text))
    }
}