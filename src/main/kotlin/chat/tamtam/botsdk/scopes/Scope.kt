package chat.tamtam.botsdk.scopes

import chat.tamtam.botsdk.client.PreparedAnswer
import chat.tamtam.botsdk.client.RequestsManager
import chat.tamtam.botsdk.client.ResultAnswer
import chat.tamtam.botsdk.client.ResultSend
import chat.tamtam.botsdk.model.CallbackId
import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.request.*
import chat.tamtam.botsdk.model.response.AttachType

interface Scope {

    val requests: RequestsManager

    suspend infix fun typingOn(chatId: ChatId) {
        requests.startTyping(chatId)
    }

    infix fun typingOff(chatId: ChatId) {
        requests.stopTyping(chatId)
    }

    private suspend fun send(userId: UserId, sendMessage: SendMessage): ResultSend =
        requests.send(userId, sendMessage)

    private suspend fun send(chatId: ChatId, sendMessage: SendMessage): ResultSend =
        requests.send(chatId, sendMessage)

    suspend infix fun SendMessage.sendFor(userId: UserId): ResultSend = send(userId, this)

    suspend infix fun String.sendFor(userId: UserId): ResultSend = SendMessage(this).sendFor(userId)

    suspend infix fun SendMessage.sendFor(chatId: ChatId): ResultSend = send(chatId, this)

    suspend infix fun String.sendFor(chatId: ChatId): ResultSend = SendMessage(this).sendFor(chatId)

    infix fun String.prepareFor(userId: UserId): SendParams {
        return SendParams(userId, sendMessage = SendMessage(this))
    }

    infix fun String.prepareFor(chatId: ChatId): SendParams {
        return SendParams(chatId = chatId, sendMessage = SendMessage(this))
    }

    suspend infix fun SendParams.sendWith(keyboard: InlineKeyboard) {
        val attaches = listOf(AttachmentKeyboard(AttachType.INLINE_KEYBOARD.value.toLowerCase(), keyboard))
        send(userId, SendMessage(sendMessage.text, attaches, sendMessage.notifyUser))
    }

    suspend infix fun String.prepareReplacementCurrentMessage(answerParams: AnswerParams): PreparedAnswer {
        val answerCallback = AnswerCallback(message = SendMessage(this))
        return PreparedAnswer(answerCallback, answerParams)
    }

    suspend infix fun String.answerFor(callbackId: CallbackId): ResultAnswer {
        val answerCallback = AnswerCallback(message = SendMessage(this))
        return requests.answer(callbackId, answerCallback)
    }

    suspend infix fun String.replaceCurrentMessage(callbackId: CallbackId): ResultAnswer {
        val answerCallback = AnswerCallback(message = SendMessage(this))
        return requests.answer(callbackId, answerCallback)
    }

    suspend infix fun String.answerLikeNotification(answerParams: AnswerParams): ResultAnswer {
        val answerCallback = AnswerNotificationCallback(answerParams.userId.id, notification = this)
        return requests.answer(answerParams.callbackId, answerCallback)
    }

    suspend infix fun PreparedAnswer.answerWith(keyboard: InlineKeyboard): ResultAnswer {
        val answerCallback = AnswerCallback(SendMessage(answerCallback.message.text, listOf(AttachmentKeyboard(AttachType.INLINE_KEYBOARD.value, keyboard))))
        return requests.answer(answerParams.callbackId, answerCallback)
    }
}