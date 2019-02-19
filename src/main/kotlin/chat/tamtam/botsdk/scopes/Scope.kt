package chat.tamtam.botsdk.scopes

import chat.tamtam.botsdk.client.PreparedAnswer
import chat.tamtam.botsdk.client.RequestsManager
import chat.tamtam.botsdk.client.ResultAnswer
import chat.tamtam.botsdk.client.ResultSend
import chat.tamtam.botsdk.client.ResultUploadUrl
import chat.tamtam.botsdk.model.CallbackId
import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.ImageUrl
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.VideoUrl
import chat.tamtam.botsdk.model.request.AnswerCallback
import chat.tamtam.botsdk.model.request.AnswerNotificationCallback
import chat.tamtam.botsdk.model.request.AnswerParams
import chat.tamtam.botsdk.model.request.AttachmentKeyboard
import chat.tamtam.botsdk.model.request.InlineKeyboard
import chat.tamtam.botsdk.model.request.SendMessage
import chat.tamtam.botsdk.model.request.SendParams
import chat.tamtam.botsdk.model.request.UploadParams
import chat.tamtam.botsdk.model.request.createAnswerCallbackForImageUrl
import chat.tamtam.botsdk.model.request.createAnswerCallbackForKeyboard
import chat.tamtam.botsdk.model.request.createAnswerCallbackForVideoUrl
import chat.tamtam.botsdk.model.request.createSendMessageForImageUrl
import chat.tamtam.botsdk.model.request.createSendMessageForVideoUrl
import chat.tamtam.botsdk.model.response.AttachType

interface Scope {

    val requests: RequestsManager

    suspend infix fun typingOn(chatId: ChatId) {
        requests.startTyping(chatId)
    }

    infix fun typingOff(chatId: ChatId) {
        requests.stopTyping(chatId)
    }

    suspend infix fun SendMessage.sendFor(userId: UserId): ResultSend = requests.send(userId, this)

    suspend infix fun String.sendFor(userId: UserId): ResultSend = SendMessage(this).sendFor(userId)

    suspend infix fun SendMessage.sendFor(chatId: ChatId): ResultSend = requests.send(chatId, this)

    suspend infix fun String.sendFor(chatId: ChatId): ResultSend = SendMessage(this).sendFor(chatId)

    infix fun String.prepareFor(userId: UserId): SendParams {
        return SendParams(userId, sendMessage = SendMessage(this))
    }

    infix fun String.prepareFor(chatId: ChatId): SendParams {
        return SendParams(chatId = chatId, sendMessage = SendMessage(this))
    }

    suspend infix fun SendParams.sendWith(keyboard: InlineKeyboard) {
        val attaches = listOf(AttachmentKeyboard(AttachType.INLINE_KEYBOARD.value.toLowerCase(), keyboard))
        requests.send(userId, SendMessage(sendMessage.text, attaches, sendMessage.notifyUser))
    }

    suspend infix fun SendParams.sendWith(imageUrl: ImageUrl): ResultSend {
        val sendMessage = createSendMessageForImageUrl(sendMessage, imageUrl)
        return requests.send(chatId, sendMessage)
    }

    suspend infix fun SendParams.sendWith(videoUrl: VideoUrl): ResultSend {
        val sendMessage = createSendMessageForVideoUrl(sendMessage, videoUrl)
        return requests.send(chatId, sendMessage)
    }

    suspend infix fun SendParams.sendWith(uploadParams: UploadParams): ResultSend {
        val resultUpload = requests.getUploadUrl(uploadParams.uploadType)
        return when (resultUpload) {
            is ResultUploadUrl.Success -> requests.sendWithUpload(chatId, sendMessage, resultUpload.response, uploadParams)
            is ResultUploadUrl.Failure -> ResultSend.Failure(resultUpload.exception)
        }
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
        val answerCallback = createAnswerCallbackForKeyboard(answerCallback.message, keyboard)
        return requests.answer(answerParams.callbackId, answerCallback)
    }

    suspend infix fun PreparedAnswer.answerWith(imageUrl: ImageUrl): ResultAnswer {
        val answerCallback = createAnswerCallbackForImageUrl(answerCallback.message, imageUrl)
        return requests.answer(answerParams.callbackId, answerCallback)
    }

    suspend infix fun PreparedAnswer.answerWith(videoUrl: VideoUrl): ResultAnswer {
        val answerCallback = createAnswerCallbackForVideoUrl(answerCallback.message, videoUrl)
        return requests.answer(answerParams.callbackId, answerCallback)
    }

    suspend infix fun PreparedAnswer.answerWith(uploadParams: UploadParams): ResultAnswer {
        val resultUpload = requests.getUploadUrl(uploadParams.uploadType)
        return when (resultUpload) {
            is ResultUploadUrl.Success -> requests.answerWithUpload(answerParams.callbackId, answerCallback.message, resultUpload.response, uploadParams)
            is ResultUploadUrl.Failure -> ResultAnswer.Failure(resultUpload.exception)
        }
    }
}