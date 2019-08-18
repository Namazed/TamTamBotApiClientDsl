package chat.tamtam.botsdk.scopes

import chat.tamtam.botsdk.client.RequestsManager
import chat.tamtam.botsdk.client.ResultRequest
import chat.tamtam.botsdk.model.AttachType
import chat.tamtam.botsdk.model.CallbackId
import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.ImageUrl
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.prepared.ChatMembersList
import chat.tamtam.botsdk.model.prepared.Message
import chat.tamtam.botsdk.model.request.AnswerCallback
import chat.tamtam.botsdk.model.request.AnswerParams
import chat.tamtam.botsdk.model.request.AttachmentKeyboard
import chat.tamtam.botsdk.model.request.EMPTY_INLINE_KEYBOARD
import chat.tamtam.botsdk.model.request.InlineKeyboard
import chat.tamtam.botsdk.model.request.LinkOnMessage
import chat.tamtam.botsdk.model.request.ReusableMediaParams
import chat.tamtam.botsdk.model.request.PreparedAnswer
import chat.tamtam.botsdk.model.request.SendParams
import chat.tamtam.botsdk.model.request.UploadParams
import chat.tamtam.botsdk.model.request.UploadType
import chat.tamtam.botsdk.model.request.createAnswerCallbackForImageUrl
import chat.tamtam.botsdk.model.request.createAnswerCallbackForKeyboard
import chat.tamtam.botsdk.model.request.createAnswerCallbackForReusablePhotoToken
import chat.tamtam.botsdk.model.request.createSendMessageForImageUrl
import chat.tamtam.botsdk.model.response.Default
import chat.tamtam.botsdk.model.request.SendMessage as RequestSendMessage

/**
 * This interface give all requests methods from BotAPI in dsl style and no dsl (with help [requests] property)
 */
interface Scope {

    /**
     * This property need if you want use request methods without dsl style
     * like [RequestsManager.send] or [RequestsManager.answer] or something else
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
     * @see [RequestsManager.getAllMessages]
     * @receiver - this is a count of messages [0..100]
     */
    suspend infix fun Int.messagesIn(chatId: ChatId): ResultRequest<List<Message>> = requests.getAllMessages(chatId, count = this)

    /**
     * @see [RequestsManager.getMembers]
     * @receiver - this is a count of members [0..100]
     */
    suspend infix fun Int.membersIn(chatId: ChatId): ResultRequest<ChatMembersList> = requests.getMembers(chatId, count = this)

    /**
     * @see [RequestsManager.getMembers]
     * @receiver - this is a list of users identifiers to get their membership.
     */
    suspend infix fun List<UserId>.membersIn(chatId: ChatId): ResultRequest<ChatMembersList> = requests.getMembers(chatId, usersIds = this)

    /**
     * This method need for send [RequestSendMessage] for User by userId
     *
     * @param userId - this is inline class [UserId] which contains userId
     * @receiver - this is [RequestSendMessage] which you send for User
     * @return - [ResultRequest] which contains Success with current response from server or Failure with [retrofit2.HttpException] or [Exception]
     */
    suspend infix fun RequestSendMessage.sendFor(userId: UserId): ResultRequest<Message> = requests.send(userId, this)

    /**
     * This method need for send [RequestSendMessage] with text of message for User by userId
     *
     * @param userId - this is inline class [UserId] which contains userId
     * @receiver - this is text for message which you send for User
     * @return - [ResultRequest] which contains Success with current response from server or Failure with [retrofit2.HttpException] or [Exception]
     */
    suspend infix fun String.sendFor(userId: UserId): ResultRequest<Message> = RequestSendMessage(this).sendFor(userId)

    /**
     * This method need for send [RequestSendMessage] with text of message for User by userId
     *
     * @param userId - this is inline class [UserId] which contains userId
     * @receiver - this is [LinkOnMessage] which you send for User, link with type like forward or reply
     * @return - [ResultRequest] which contains Success with current response from server or Failure with [retrofit2.HttpException] or [Exception]
     */
    suspend infix fun LinkOnMessage.sendFor(userId: UserId): ResultRequest<Message> = RequestSendMessage(link = this).sendFor(userId)

    /**
     * This method need for send [RequestSendMessage] with text of message for User by userId
     *
     * @param chatId - this is inline class [ChatId] which contains chatId
     * @receiver - this is [LinkOnMessage] which you send for Chat, link with type like forward or reply
     * @return - [ResultRequest] which contains Success with current response from server or Failure with [retrofit2.HttpException] or [Exception]
     */
    suspend infix fun LinkOnMessage.sendFor(chatId: ChatId): ResultRequest<Message> = RequestSendMessage(link = this).sendFor(chatId)

    /**
     * This method need for send [RequestSendMessage] with text of message for Chat by chatId
     *
     * @param chatId - this is inline class [ChatId] which contains chatId
     * @receiver - this is [RequestSendMessage] which you send for Chat
     * @return - [ResultRequest] which contains Success with current response from server or Failure with [retrofit2.HttpException] or [Exception]
     */
    suspend infix fun RequestSendMessage.sendFor(chatId: ChatId): ResultRequest<Message> = requests.send(chatId, this)

    /**
     * This method need for send [RequestSendMessage] with text of message for Chat by chatId
     * You should know that this method notify all users in chat.
     *
     * @param chatId - this is inline class [ChatId] which contains chatId
     * @receiver - this is text for message which you send for Chat
     * @return - [ResultRequest] which contains Success with current response from server or Failure with [retrofit2.HttpException] or [Exception]
     */
    suspend infix fun String.sendFor(chatId: ChatId): ResultRequest<Message> = RequestSendMessage(this).sendFor(chatId)

    /**
     * This method need for connect with [sendWith] and pass [SendParams]
     * You should use it only if you want send message with attaches: [InlineKeyboard] or other
     *
     * @param userId - this is inline class [UserId] which contains userId
     * @receiver - this is text for message which you send for User after call [sendWith]
     * @return - [SendParams] which contains [UserId] or [ChatId] and [RequestSendMessage] which contains text of message
     */
    infix fun String.prepareFor(userId: UserId): SendParams {
        return SendParams(userId, sendMessage = RequestSendMessage(this))
    }

    /**
     * This method need for connect with [sendWith] and pass [SendParams]
     * You should use it only if you want send message with attaches: [InlineKeyboard] or other
     * This method always notify all users in chat.
     *
     * @param chatId - this is inline class [ChatId] which contains chatId
     * @receiver - this is text for message which you send for Chat after call [sendWith]
     * @return - [SendParams] which contains [UserId] or [ChatId] and [RequestSendMessage] which contains text of message
     */
    infix fun String.prepareFor(chatId: ChatId): SendParams {
        return SendParams(chatId = chatId, sendMessage = RequestSendMessage(this))
    }

    /**
     * This method send message with contains data from [SendParams] and [InlineKeyboard] for User
     *
     * @param link - this is [LinkOnMessage] which you send for Chat or User, link with type like forward or reply
     * @receiver - this is [SendParams] which contains [UserId] and [RequestSendMessage] which contains text of message
     * @return - [ResultRequest] which contains Success with current response from server or Failure with [retrofit2.HttpException] or [Exception]
     */
    suspend infix fun SendParams.sendWith(link: LinkOnMessage): ResultRequest<Message> {
        return sendForUserOrChat(userId, chatId, RequestSendMessage(sendMessage.text, notifyUser = sendMessage.notifyUser, link = link))
    }

    /**
     * This method send message with contains data from [SendParams] and [InlineKeyboard] for User
     *
     * @param keyboard - this is [InlineKeyboard] which you want send for User
     * @receiver - this is [SendParams] which contains [UserId] and [RequestSendMessage] which contains text of message
     * @return - [ResultRequest] which contains Success with current response from server or Failure with [retrofit2.HttpException] or [Exception]
     */
    suspend infix fun SendParams.sendWith(keyboard: InlineKeyboard): ResultRequest<Message> {
        val attaches = if (keyboard == EMPTY_INLINE_KEYBOARD) {
            emptyList()
        } else {
            listOf(AttachmentKeyboard(AttachType.INLINE_KEYBOARD.value.toLowerCase(), keyboard))
        }
        return sendForUserOrChat(userId, chatId, RequestSendMessage(sendMessage.text, attaches, sendMessage.notifyUser))
    }

    /**
     * This method send message with contains data from [SendParams] and [ImageUrl] for Chat
     *
     * @param imageUrl - this is inline class [ImageUrl] which contains url on Image, which you want send in Chat
     * @receiver - this is [SendParams] which contains [ChatId] and [RequestSendMessage] which contains text of message
     * @return - [ResultRequest] which contains Success with current response from server or Failure with [retrofit2.HttpException] or [Exception]
     */
    suspend infix fun SendParams.sendWith(imageUrl: ImageUrl): ResultRequest<Message> {
        val sendMessage = createSendMessageForImageUrl(sendMessage, imageUrl)
        return sendForUserOrChat(userId, chatId, sendMessage)
    }

    /**
     * You need use it if you have reusableId or reusableFileId!
     * This method send message with contains data from [SendParams] and [UploadParams] for Chat
     *
     * @param reusablePhotoToken - you get it after you send uploaded photo somewhere
     * @receiver - this is [SendParams] which contains [ChatId] and [RequestSendMessage] which contains text of message
     * @return - look at [RequestsManager.send]
     */
    suspend infix fun SendParams.sendWith(reusableMediaParams: ReusableMediaParams): ResultRequest<Message> {
        return requests.sendMedia(this, reusableMediaParams)
    }

    /**
     * This method send message with contains data from [SendParams] and [UploadParams] for Chat
     *
     * @param uploadParams - this is inline class [UploadParams] which contains path on file in you system and [UploadType]
     * @receiver - this is [SendParams] which contains [ChatId] and [RequestSendMessage] which contains text of message
     * @return - look at [RequestsManager.send]
     */
    suspend infix fun SendParams.sendWith(uploadParams: UploadParams): ResultRequest<Message> {
        val resultUpload = requests.getUploadUrl(uploadParams.uploadType)
        return when (resultUpload) {
            is ResultRequest.Success -> requests.sendWithUpload(
                chatId,
                userId,
                sendMessage,
                resultUpload.response,
                uploadParams
            )
            is ResultRequest.Failure -> ResultRequest.Failure(resultUpload.httpStatusCode, resultUpload.error, resultUpload.exception)
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
        val answerCallback = AnswerCallback(message = RequestSendMessage(this))
        return PreparedAnswer(answerCallback, answerParams)
    }

    /**
     * This method answer on Callback by [CallbackId] with new message for replace old message
     *
     * @param callbackId - this is inline class [CallbackId] which contains callbackId
     * @receiver - this is text for new message, which replace old message
     * @return - look at [RequestsManager.answer]
     */
    suspend infix fun String.answerFor(callbackId: CallbackId): ResultRequest<Default> {
        val answerCallback = AnswerCallback(message = RequestSendMessage(this))
        return requests.answer(callbackId, answerCallback)
    }

    /**
     * @see [answerFor]
     */
    suspend infix fun String.replaceCurrentMessage(callbackId: CallbackId): ResultRequest<Default> {
        val answerCallback = AnswerCallback(message = RequestSendMessage(this))
        return requests.answer(callbackId, answerCallback)
    }

    /**
     * This method answer on Callback by [CallbackId] with notification for specific user by [UserId]
     *
     * @param answerParams - [AnswerParams] which contains [CallbackId] of [InlineKeyboard] and [UserId]
     * @receiver - this is notification text
     * @return - look at [RequestsManager.answer]
     */
    suspend infix fun String.answerNotification(answerParams: AnswerParams): ResultRequest<Default> {
        val answerCallback = AnswerCallback(userId = answerParams.userId.id, notification = this)
        return requests.answer(answerParams.callbackId, answerCallback)
    }

    /**
     * This method answer on Callback by [CallbackId] with new message which contains InlineKeyboard
     *
     * @param keyboard - attach [InlineKeyboard], replace attaches in old message with CallbackId
     * @receiver - this is [PreparedAnswer] which contains [AnswerParams] and [AnswerCallback]
     *
     * @return - look at [RequestsManager.answer]
     */
    suspend infix fun PreparedAnswer.answerWith(keyboard: InlineKeyboard): ResultRequest<Default> {
        val answerCallback = answerCallback.message?.let {
            createAnswerCallbackForKeyboard(it, keyboard)
        } ?: return getFailureForNullableMessage()
        return requests.answer(answerParams.callbackId, answerCallback)
    }

    /**
     * This method answer on Callback by [CallbackId] with new message which contains Image attach
     *
     * @param imageUrl - this url send with new message and upload image, replace attaches in old message with CallbackId
     * @receiver - this is [PreparedAnswer] which contains [AnswerParams] and [AnswerCallback]
     *
     * @return - look at [RequestsManager.answer]
     */
    suspend infix fun PreparedAnswer.answerWith(imageUrl: ImageUrl): ResultRequest<Default> {
        val answerCallback = answerCallback.message?.let {
            createAnswerCallbackForImageUrl(it, imageUrl)
        } ?: return getFailureForNullableMessage()
        return requests.answer(answerParams.callbackId, answerCallback)
    }

    fun getFailureForNullableMessage(): ResultRequest.Failure<Default> =
        ResultRequest.Failure(-1, null, IllegalStateException("Message mustn't be null"))

    /**
     * You need use it if you have reusablePhotoToken!
     * This method answer on Callback by [CallbackId] with new message which contains Image attach
     *
     * @param reusablePhotoToken - you get it after you send uploaded photo somewhere
     * @receiver - this is [PreparedAnswer] which contains [AnswerParams] and [AnswerCallback]
     *
     * @return - look at [RequestsManager.answer]
     */
    suspend infix fun PreparedAnswer.answerWith(reusablePhotoToken: String): ResultRequest<Default> {
        val answerCallback = answerCallback.message?.let {
            createAnswerCallbackForReusablePhotoToken(it, reusablePhotoToken)
        } ?: return getFailureForNullableMessage()
        return requests.answer(answerParams.callbackId, answerCallback)
    }

    /**
     * This method answer on Callback by [CallbackId] with new message which contains media attach
     * You should use this method if you need upload local Media to server by File path and then answer on Callback with uploaded media.
     *
     * @param uploadParams - this class contains local path of File (Media) which you want upload to server and contains type of media.
     * @receiver - this is [PreparedAnswer] which contains [AnswerParams] and [AnswerCallback]
     *
     * @return - look at [RequestsManager.answer]
     */
    suspend infix fun PreparedAnswer.answerWith(uploadParams: UploadParams): ResultRequest<Default> {
        val resultUpload = requests.getUploadUrl(uploadParams.uploadType)
        return when (resultUpload) {
            is ResultRequest.Success -> {
                answerCallback.message?.let {
                    requests.answerWithUpload(answerParams.callbackId, it, resultUpload.response, uploadParams)
                } ?: return getFailureForNullableMessage()
            }
            is ResultRequest.Failure -> ResultRequest.Failure(resultUpload.httpStatusCode, resultUpload.error, resultUpload.exception)
        }
    }

    /**
     * This method prepare notification
     * This method use only like connector for [answerWith] methods, which contains parameter [RequestSendMessage]
     *
     * You need use it if you want answer on Callback with notification and message in one time
     *
     * @param answerParams - [AnswerParams] which contains [CallbackId] of [InlineKeyboard] and [UserId]
     * @receiver - this is text for notification (Toast) for User
     * @return - [PreparedAnswer] which contains [AnswerParams] and [AnswerCallback]
     */
    suspend infix fun String.prepareNotification(answerParams: AnswerParams): PreparedAnswer {
        val answerCallback = AnswerCallback(notification = this, userId = answerParams.userId.id)
        return PreparedAnswer(answerCallback, answerParams)
    }

    /**
     * This method answer on Callback by [CallbackId] with new message
     * You can use it, if you want answer with notification and message in one time.
     *
     * @param sendMessage - message which you want send
     * @receiver - this is [PreparedAnswer] which contains [AnswerParams] and [AnswerCallback]
     *
     * @return - look at [RequestsManager.answer]
     */
    suspend infix fun PreparedAnswer.answerWith(sendMessage: RequestSendMessage): ResultRequest<Default> {
        return requests.answer(answerParams.callbackId, AnswerCallback(sendMessage, answerCallback.userId, answerCallback.notification))
    }

    private suspend fun sendForUserOrChat(userId: UserId, chatId: ChatId, sendMessage: RequestSendMessage): ResultRequest<Message> {
        check(chatId.id != -1L || userId.id != -1L) {
            "ChatId or UserId must be correct, current both are -1L"
        }
        return if (chatId.id == -1L) {
            requests.send(userId, sendMessage)
        } else {
            requests.send(chatId, sendMessage)
        }
    }
}