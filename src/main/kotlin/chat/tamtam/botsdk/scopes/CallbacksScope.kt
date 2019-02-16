package chat.tamtam.botsdk.scopes

import chat.tamtam.botsdk.client.RequestsManager
import chat.tamtam.botsdk.model.Payload
import chat.tamtam.botsdk.model.request.AnswerParams
import chat.tamtam.botsdk.model.request.AttachmentKeyboard
import chat.tamtam.botsdk.model.request.InlineKeyboard
import chat.tamtam.botsdk.model.response.AttachType
import chat.tamtam.botsdk.state.CallbackState
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import chat.tamtam.botsdk.model.request.AnswerCallback as RequestAnswerCallback
import chat.tamtam.botsdk.model.request.SendMessage as RequestSendMessage

@BotMarker
class CallbacksScope(
    val requests: RequestsManager,
    private val log: Logger = LoggerFactory.getLogger(CallbacksScope::class.java.name),
    private var defaultAnswer: suspend (CallbackState) -> Unit = {},
    private val callbacksAnswers: MutableMap<String, suspend (CallbackState) -> Unit> = mutableMapOf()
) {

    internal operator fun get(payloadButton: Payload) = callbacksAnswers[payloadButton.value] ?: defaultAnswer

    fun defaultAnswer(defaultAnswer: suspend (callbackState: CallbackState) -> Unit) {
        log.info("defaultAnswer: save")
        this.defaultAnswer = defaultAnswer
    }

    fun answerOnCallback(payloadButton: Payload, answer: suspend (callbackState: CallbackState) -> Unit) {
        log.info("answerOnCallback: save with payload ${payloadButton.value}")
        callbacksAnswers[payloadButton.value] = answer
    }

//    private suspend fun send(userId: UserId, sendMessage: RequestSendMessage) {
//        botHttpManager.messageApi.sendMessage(userId, sendMessage)
//    }
//
//    suspend infix fun CallbackState.send(sendMessage: RequestSendMessage) {
//        botHttpManager.messageApi.sendMessage(UserId(callback.user.userId), sendMessage)
//    }
//
//    infix fun CallbackState.createText(text: String) : Triple<CallbackId, UserId, RequestSendMessage> {
//        return Triple(CallbackId(callback.callbackId), UserId(callback.user.userId), RequestSendMessage(text))
//    }
//
//    suspend infix fun Triple<CallbackId, UserId, RequestSendMessage>.sendWith(keyboard: InlineKeyboard) {
//        val attaches = listOf(AttachmentKeyboard(AttachType.INLINE_KEYBOARD.value.toLowerCase(), keyboard))
//        send(second, RequestSendMessage(third.text, attaches, third.notifyUser))
//    }
//
//    suspend infix fun CallbackState.sendText(text: String) {
//        send(RequestSendMessage(text))
//    }
//
//    private suspend fun answer(callbackId: CallbackId, requestAnswerCallback: RequestAnswerCallback) {
//        botHttpManager.answerOnCallback(callbackId, requestAnswerCallback)
//    }
//
//    suspend infix fun Triple<CallbackId, UserId, RequestSendMessage>.answerWith(keyboard: InlineKeyboard) {
//        val attaches = listOf(AttachmentKeyboard(AttachType.INLINE_KEYBOARD.value.toLowerCase(), keyboard))
//        answer(first, RequestAnswerCallback(second.id, RequestSendMessage(third.text, attaches)))
//    }
//
//    suspend infix fun CallbackState.answer(notification: String) {
//        answer(CallbackId(callback.callbackId), RequestAnswerCallback(callback.user.userId, notification = notification))
//    }

    suspend infix fun String.prepareAnswerWith(answerParams: AnswerParams): Pair<AnswerParams, RequestSendMessage> {
        return Pair(answerParams, RequestSendMessage(this))
    }

    suspend infix fun RequestAnswerCallback.prepareAnswerWith(answerParams: AnswerParams): Pair<AnswerParams, RequestAnswerCallback> {
        return Pair(answerParams, this)
    }

    suspend infix fun Pair<AnswerParams, RequestAnswerCallback>.answerWith(keyboard: InlineKeyboard) {
        val answerCallback = RequestAnswerCallback(first.userId.id, RequestSendMessage("",
            listOf(AttachmentKeyboard(AttachType.INLINE_KEYBOARD.value, keyboard))))
        requests.answer(first.callbackId, answerCallback)
    }
}
