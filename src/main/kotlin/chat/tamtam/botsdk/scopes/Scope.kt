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
import io.ktor.client.features.BadResponseStatusException

/**
 * This interface give all requests methods from BotAPI in dsl style and no dsl (with help [requests] property)
 */
interface Scope {

    /**
     * This property need if you want use request methods without dsl style
     * like [RequestsManager.send] or [RequestsManager.answer]
     */
    val requests: RequestsManager

    /**
     * This method start usual typing in Chat
     *
     * @param chatId - this is inline class [ChatId] which contains chatId
     */
    suspend infix fun typingOn(chatId: ChatId) {
        requests.startTyping(chatId)
    }

    /**
     * This method stop usual typing in Chat
     *
     * @param chatId - this is inline class [ChatId] which contains chatId
     */
    infix fun typingOff(chatId: ChatId) {
        requests.stopTyping(chatId)
    }

    /**
     * This method need for send [SendMessage] for User by userId
     *
     * @param userId - this is inline class [UserId] which contains userId
     * @receiver - this is [SendMessage] which you send for User
     * @return - [ResultSend] which contains Success with current response from server or Failure with [BadResponseStatusException] or [Exception]
     */
    suspend infix fun SendMessage.sendFor(userId: UserId): ResultSend = requests.send(userId, this)

    /**
     * This method need for send [SendMessage] with text of message for User by userId
     *
     * @param userId - this is inline class [UserId] which contains userId
     * @receiver - this is text for message which you send for User
     * @return - [ResultSend] which contains Success with current response from server or Failure with [BadResponseStatusException] or [Exception]
     */
    suspend infix fun String.sendFor(userId: UserId): ResultSend = SendMessage(this).sendFor(userId)

    /**
     * This method need for send [SendMessage] with text of message for Chat by chatId
     *
     * @param chatId - this is inline class [ChatId] which contains chatId
     * @receiver - this is [SendMessage] which you send for Chat
     * @return - [ResultSend] which contains Success with current response from server or Failure with [BadResponseStatusException] or [Exception]
     */
    suspend infix fun SendMessage.sendFor(chatId: ChatId): ResultSend = requests.send(chatId, this)

    /**
     * This method need for send [SendMessage] with text of message for Chat by chatId
     * You should know that this method notify all users in chat.
     *
     * @param chatId - this is inline class [ChatId] which contains chatId
     * @receiver - this is text for message which you send for Chat
     * @return - [ResultSend] which contains Success with current response from server or Failure with [BadResponseStatusException] or [Exception]
     */
    suspend infix fun String.sendFor(chatId: ChatId): ResultSend = SendMessage(this).sendFor(chatId)

    /**
     * This method need for connect with [sendWith] and pass [SendParams]
     * You should use it only if you want send message with attaches: [InlineKeyboard] or other
     *
     * @param userId - this is inline class [UserId] which contains userId
     * @receiver - this is text for message which you send for User after call [sendWith]
     * @return - [SendParams] which contains [UserId] or [ChatId] and [SendMessage] which contains text of message
     */
    infix fun String.prepareFor(userId: UserId): SendParams {
        return SendParams(userId, sendMessage = SendMessage(this))
    }

    /**
     * This method need for connect with [sendWith] and pass [SendParams]
     * You should use it only if you want send message with attaches: [InlineKeyboard] or other
     * This method always notify all users in chat.
     *
     * @param chatId - this is inline class [ChatId] which contains chatId
     * @receiver - this is text for message which you send for Chat after call [sendWith]
     * @return - [SendParams] which contains [UserId] or [ChatId] and [SendMessage] which contains text of message
     */
    infix fun String.prepareFor(chatId: ChatId): SendParams {
        return SendParams(chatId = chatId, sendMessage = SendMessage(this))
    }

    /**
     * This method send message with contains data from [SendParams] and [InlineKeyboard] for User
     *
     * @param keyboard - this is [InlineKeyboard] which you want send for User
     * @receiver - this is [SendParams] which contains [UserId] and [SendMessage] which contains text of message
     * @return - [ResultSend] which contains Success with current response from server or Failure with [BadResponseStatusException] or [Exception]
     */
    suspend infix fun SendParams.sendWith(keyboard: InlineKeyboard): ResultSend {
        val attaches = listOf(AttachmentKeyboard(AttachType.INLINE_KEYBOARD.value.toLowerCase(), keyboard))
        return requests.send(userId, SendMessage(sendMessage.text, attaches, sendMessage.notifyUser))
    }

    /**
     * This method send message with contains data from [SendParams] and [ImageUrl] for Chat
     *
     * @param imageUrl - this is inline class [ImageUrl] which contains url on Image, which you want send in Chat
     * @receiver - this is [SendParams] which contains [ChatId] and [SendMessage] which contains text of message
     * @return - [ResultSend] which contains Success with current response from server or Failure with [BadResponseStatusException] or [Exception]
     */
    suspend infix fun SendParams.sendWith(imageUrl: ImageUrl): ResultSend {
        val sendMessage = createSendMessageForImageUrl(sendMessage, imageUrl)
        return requests.send(chatId, sendMessage)
    }

    /**
     * This method send message with contains data from [SendParams] and [VideoUrl] for Chat
     *
     * @param videoUrl - this is inline class [VideoUrl] which contains url on Video, which you want send in Chat
     * @receiver - this is [SendParams] which contains [ChatId] and [SendMessage] which contains text of message
     * @return - [ResultSend] which contains Success with current response from server or Failure with [BadResponseStatusException] or [Exception]
     */
    suspend infix fun SendParams.sendWith(videoUrl: VideoUrl): ResultSend {
        val sendMessage = createSendMessageForVideoUrl(sendMessage, videoUrl)
        return requests.send(chatId, sendMessage)
    }

    /**
     * This method send message with contains data from [SendParams] and [UploadParams] for Chat
     *
     * @param uploadParams - this is inline class [UploadParams] which contains path on file in you system and [UploadType]
     * @receiver - this is [SendParams] which contains [ChatId] and [SendMessage] which contains text of message
     * @return - [ResultSend] which contains Success with current response from server or Failure with [BadResponseStatusException] or [Exception]
     */
    suspend infix fun SendParams.sendWith(uploadParams: UploadParams): ResultSend {
        val resultUpload = requests.getUploadUrl(uploadParams.uploadType)
        return when (resultUpload) {
            is ResultUploadUrl.Success -> requests.sendWithUpload(chatId, sendMessage, resultUpload.response, uploadParams)
            is ResultUploadUrl.Failure -> ResultSend.Failure(resultUpload.exception)
        }
    }

    /**
     * This method prepare message which replace message with [CallbackId] from [AnswerParams]
     * This method use only like connector for [answerWith] methods.
     * You need use it if you want answer on Callback with message which contains attaches.
     *
     * @param answerParams - [AnswerParams] which contains [CallbackId] of [InlineKeyboard] and [UserId]
     * @receiver - this is text for new message, which replace old message
     * @return - [PreparedAnswer] which contains [AnswerParams] and [AnswerCallback]
     */
    suspend infix fun String.prepareReplacementCurrentMessage(answerParams: AnswerParams): PreparedAnswer {
        val answerCallback = AnswerCallback(message = SendMessage(this))
        return PreparedAnswer(answerCallback, answerParams)
    }

    /**
     * This method answer on Callback by [CallbackId] with new message
     *
     * @param callbackId - this is inline class [CallbackId] which contains callbackId
     * @receiver - this is text for new message, which replace old message
     * @return - [ResultAnswer] which contains Success with current response or Failure with [BadResponseStatusException] or [Exception]
     */
    suspend infix fun String.answerFor(callbackId: CallbackId): ResultAnswer {
        val answerCallback = AnswerCallback(message = SendMessage(this))
        return requests.answer(callbackId, answerCallback)
    }

    suspend infix fun String.replaceCurrentMessage(callbackId: CallbackId): ResultAnswer {
        val answerCallback = AnswerCallback(message = SendMessage(this))
        return requests.answer(callbackId, answerCallback)
    }

    /**
     * This method answer on Callback by [CallbackId] with notification for specific user by [UserId]
     *
     * @param answerParams - [AnswerParams] which contains [CallbackId] of [InlineKeyboard] and [UserId]
     * @receiver - this is notification text
     * @return - [ResultAnswer] which contains Success with current response or Failure with [BadResponseStatusException] or [Exception]
     */
    suspend infix fun String.answerNotification(answerParams: AnswerParams): ResultAnswer {
        val answerCallback = AnswerNotificationCallback(answerParams.userId.id, notification = this)
        return requests.answer(answerParams.callbackId, answerCallback)
    }

    /**
     * This method answer on Callback by [CallbackId] with new message which contains InlineKeyboard
     *
     * @param keyboard - attach [InlineKeyboard], replace attaches in old message with CallbackId
     * @receiver - this is [PreparedAnswer] which contains [AnswerParams] and [AnswerCallback]
     * @return - [ResultAnswer] which contains Success with current response from server or Failure with [BadResponseStatusException] or [Exception]
     */
    suspend infix fun PreparedAnswer.answerWith(keyboard: InlineKeyboard): ResultAnswer {
        val answerCallback = createAnswerCallbackForKeyboard(answerCallback.message, keyboard)
        return requests.answer(answerParams.callbackId, answerCallback)
    }

    /**
     * This method answer on Callback by [CallbackId] with new message which contains Image attach
     *
     * @param imageUrl - this url send with new message and upload image, replace attaches in old message with CallbackId
     * @receiver - this is [PreparedAnswer] which contains [AnswerParams] and [AnswerCallback]
     * @return - [ResultAnswer] which contains Success with current response from server or Failure with [BadResponseStatusException] or [Exception]
     */
    suspend infix fun PreparedAnswer.answerWith(imageUrl: ImageUrl): ResultAnswer {
        val answerCallback = createAnswerCallbackForImageUrl(answerCallback.message, imageUrl)
        return requests.answer(answerParams.callbackId, answerCallback)
    }

    /**
     * This method answer on Callback by [CallbackId] with new message which contains Video attach
     *
     * @param videoUrl - this url send with new message and upload video, replace attaches in old message with CallbackId
     * @receiver - this is [PreparedAnswer] which contains [AnswerParams] and [AnswerCallback]
     * @return - [ResultAnswer] which contains Success with current response from server or Failure with [BadResponseStatusException] or [Exception]
     */
    suspend infix fun PreparedAnswer.answerWith(videoUrl: VideoUrl): ResultAnswer {
        val answerCallback = createAnswerCallbackForVideoUrl(answerCallback.message, videoUrl)
        return requests.answer(answerParams.callbackId, answerCallback)
    }

    /**
     * This method answer on Callback by [CallbackId] with new message which contains media attach
     * You should use this method if you need upload local Media to server by File path and then answer on Callback with uploaded media.
     *
     * @param uploadParams - this class contains local path of File (Media) which you want upload to server and contains type of media.
     * @receiver - this is [PreparedAnswer] which contains [AnswerParams] and [AnswerCallback]
     * @return - [ResultAnswer] which contains Success with current response from server or Failure with [BadResponseStatusException] or [Exception]
     */
    suspend infix fun PreparedAnswer.answerWith(uploadParams: UploadParams): ResultAnswer {
        val resultUpload = requests.getUploadUrl(uploadParams.uploadType)
        return when (resultUpload) {
            is ResultUploadUrl.Success -> requests.answerWithUpload(answerParams.callbackId, answerCallback.message, resultUpload.response, uploadParams)
            is ResultUploadUrl.Failure -> ResultAnswer.Failure(resultUpload.exception)
        }
    }
}