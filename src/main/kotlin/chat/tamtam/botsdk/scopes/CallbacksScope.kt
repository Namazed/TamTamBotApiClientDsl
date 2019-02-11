package chat.tamtam.botsdk.scopes

import chat.tamtam.botsdk.client.BotHttpManager
import chat.tamtam.botsdk.model.CallbackId
import chat.tamtam.botsdk.model.Payload
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.request.AttachmentKeyboard
import chat.tamtam.botsdk.model.request.InlineKeyboard
import chat.tamtam.botsdk.model.response.AttachType
import chat.tamtam.botsdk.model.response.Callback
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import chat.tamtam.botsdk.model.request.AnswerCallback as RequestAnswerCallback
import chat.tamtam.botsdk.model.request.SendMessage as RequestSendMessage

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

    private suspend fun send(userId: UserId, sendMessage: RequestSendMessage) {
        botHttpManager.messageApi.sendMessage(userId, sendMessage)
    }

    suspend infix fun Callback.send(sendMessage: RequestSendMessage) {
        botHttpManager.messageApi.sendMessage(UserId(this.user.userId), sendMessage)
    }

    infix fun Callback.createText(text: String) : Triple<CallbackId, UserId, RequestSendMessage> {
        return Triple(CallbackId(this.callbackId), UserId(user.userId), RequestSendMessage(text))
    }

    suspend infix fun Triple<CallbackId, UserId, RequestSendMessage>.sendWith(keyboard: InlineKeyboard) {
        val attaches = listOf(AttachmentKeyboard(AttachType.INLINE_KEYBOARD.value.toLowerCase(), keyboard))
        send(second, RequestSendMessage(third.text, attaches, third.notifyUser))
    }

    suspend infix fun Callback.sendText(text: String) {
        send(RequestSendMessage(text))
    }

    private suspend fun answer(callbackId: CallbackId, requestAnswerCallback: RequestAnswerCallback) {
        botHttpManager.answerOnCallback(callbackId, requestAnswerCallback)
    }

    suspend infix fun Triple<CallbackId, UserId, RequestSendMessage>.answerWith(keyboard: InlineKeyboard) {
        val attaches = listOf(AttachmentKeyboard(AttachType.INLINE_KEYBOARD.value.toLowerCase(), keyboard))
        answer(first, RequestAnswerCallback(second.id, RequestSendMessage(third.text, attaches)))
    }

    suspend infix fun Callback.answer(notification: String) {
        answer(CallbackId(this.callbackId), RequestAnswerCallback(this.user.userId, notification = notification))
    }
}
