package chat.tamtam.botsdk.client

import chat.tamtam.botsdk.client.retrofit.Failure
import chat.tamtam.botsdk.client.retrofit.HttpResult
import chat.tamtam.botsdk.client.retrofit.Success
import chat.tamtam.botsdk.model.CallbackId
import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.MessageId
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.map
import chat.tamtam.botsdk.model.mapOrNull
import chat.tamtam.botsdk.model.prepared.ChatMembersList
import chat.tamtam.botsdk.model.prepared.ChatsList
import chat.tamtam.botsdk.model.request.AnswerCallback
import chat.tamtam.botsdk.model.request.Bot
import chat.tamtam.botsdk.model.request.ChatInfo
import chat.tamtam.botsdk.model.request.ReusableMediaParams
import chat.tamtam.botsdk.model.request.SendParams
import chat.tamtam.botsdk.model.request.UploadParams
import chat.tamtam.botsdk.model.request.UploadType
import chat.tamtam.botsdk.model.request.createAnswerCallbackForImageToken
import chat.tamtam.botsdk.model.request.createAnswerCallbackForMediaToken
import chat.tamtam.botsdk.model.request.createSendMessageForImageToken
import chat.tamtam.botsdk.model.request.createSendMessageForMediaToken
import chat.tamtam.botsdk.model.request.createSendMessageForReusableMedia
import chat.tamtam.botsdk.model.response.Default
import chat.tamtam.botsdk.model.response.Error
import chat.tamtam.botsdk.model.response.Upload
import chat.tamtam.botsdk.model.response.UploadInfo
import chat.tamtam.botsdk.typing.TypingController
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import retrofit2.Response
import chat.tamtam.botsdk.model.prepared.Bot as PreparedBot
import chat.tamtam.botsdk.model.prepared.Chat as PreparedChat
import chat.tamtam.botsdk.model.prepared.ChatMember as PreparedChatMember
import chat.tamtam.botsdk.model.prepared.Message as PreparedMessage
import chat.tamtam.botsdk.model.request.SendMessage as RequestSendMessage
import chat.tamtam.botsdk.model.request.Subscription as RequestSubscription
import chat.tamtam.botsdk.model.response.Subscription as ResponseSubscription

/**
 * This class contains all requests for Bot Api, you can use it from all scopes [chat.tamtam.botsdk.scopes.Scope]
 */
class RequestsManager internal constructor(
    private val httpManager: HttpManager,
    private val typingController: TypingController
) {

    /**
     * If you want get information about your bot, which like name or id, you need use this method
     */
    suspend fun getBotInfo(): ResultRequest<PreparedBot> = startRequest {
        httpManager.getBotInfo()
    }

    /**
     * If you want change information about your bot, which like description or commands, you need use this method
     */
    suspend fun editBotInfo(botInfo: Bot): ResultRequest<PreparedBot> = startRequest {
        httpManager.editBotInfo(botInfo)
    }

    /**
     * If you want get information about your bot in chat (admin, owner, joinTime or etc), you need use this method
     */
    suspend fun getMembershipInfoInChat(chatId: ChatId): ResultRequest<PreparedChatMember> = startRequest {
        httpManager.chatHttpManager.getChatMembershipInfo(chatId)
    }

    /**
     * This method give you all your subscriptions [ResponseSubscription]
     */
    suspend fun getAllSubscriptions(): ResultRequest<List<ResponseSubscription>> = startRequest {
        httpManager.subscriptionHttpManager.getSubscriptions()
    }

    /**
     * If you want subscribe your url on updates, use this method.
     */
    suspend fun subscribe(requestSubscription: RequestSubscription): ResultRequest<Default> = startRequest {
        httpManager.subscriptionHttpManager.subscribe(requestSubscription)
    }

    /**
     * If you want unsubscribe your url from updates, use this method.
     */
    suspend fun unsubscribe(url: String): ResultRequest<Default> = startRequest {
        httpManager.subscriptionHttpManager.unsubscribe(url)
    }

    /**
     * This method give you chats from specific marker or without.
     */
    suspend fun getAllChats(count: Int = 50, marker: Long? = null): ResultRequest<ChatsList> = startRequest {
        checkCount(count)
        httpManager.chatHttpManager.getAllChats(count, marker)
    }

    /**
     * This method give you chat by chat id
     */
    suspend fun getChat(chatId: ChatId): ResultRequest<PreparedChat> = startRequest {
        httpManager.chatHttpManager.getChat(chatId)
    }

    /**
     * If you want that your bot leave from specific chat, use this method
     */
    suspend fun leaveChat(chatId: ChatId): ResultRequest<Default> = startRequest {
        httpManager.chatHttpManager.leaveChat(chatId)
    }

    /**
     * This method give you members from specific chat. By default from first page with 20 count.
     */
    suspend fun getMembers(
        chatId: ChatId,
        count: Int = 20,
        marker: Long? = null,
        usersIds: List<UserId>? = null
    ): ResultRequest<ChatMembersList> = startRequest {
        httpManager.chatHttpManager.getMembers(chatId, count, marker, usersIds.mapOrNull())
    }

    /**
     * If you want added some users to chat, use this method.
     * Max count users for one request is 100.
     */
    suspend fun addMemebersToChat(chatId: ChatId, usersIds: List<Long>): ResultRequest<Default> = startRequest {
        httpManager.chatHttpManager.addMembers(chatId, usersIds)
    }

    /**
     * If you want removed user from chat, use this method.
     */
    suspend fun removeMemberFromChat(chatId: ChatId, userId: UserId): ResultRequest<Default> = startRequest {
        httpManager.chatHttpManager.removeMember(chatId, userId)
    }

    /**
     * If you want change chat's title or icon, use this method.
     *
     * @param chatId - this is inline class [ChatId] which contains chatId
     * @param chatInfo - this class contains new title or icon for chat [Icon]
     */
    suspend fun editChatInfo(chatId: ChatId, chatInfo: ChatInfo): ResultRequest<PreparedChat> = startRequest {
        httpManager.chatHttpManager.editChatInfo(chatId, chatInfo)
    }

    /**
     * This method give you messages from specific chat by chat id.
     * You need give period of time for given messages, and count of messages.
     */
    suspend fun getAllMessages(
        chatId: ChatId,
        messagesIds: List<MessageId>? = null,
        fromTime: Long? = null,
        toTime: Long? = null,
        count: Int = 50
    ): ResultRequest<List<PreparedMessage>> = startRequest {
        checkCount(count)
        httpManager.messageHttpManager.getAllMessages(chatId, messagesIds, fromTime, toTime, count)
    }

    /**
     * If you want delete message in a dialog or in a chat (bot must has permission to delete messages), use this method
     */
    suspend fun deleteMessage(messageId: MessageId): ResultRequest<Default> = startRequest {
        httpManager.messageHttpManager.deleteMessage(messageId)
    }

    /**
     * This method need for send [RequestSendMessage] for User by userId
     *
     * @param userId - this is inline class [UserId] which contains userId
     * @param sendMessage - this is [RequestSendMessage] which you send for User
     *
     * @return - [ResultRequest] which contains Success with current response from server or Failure with [HttpException] or [Exception]
     */
    suspend fun send(userId: UserId, sendMessage: RequestSendMessage): ResultRequest<PreparedMessage> = startRequest {
        httpManager.messageHttpManager.sendMessage(userId, sendMessage)
    }

    /**
     * @see [send]
     */
    suspend fun send(chatId: ChatId, sendMessage: RequestSendMessage): ResultRequest<PreparedMessage> = startRequest {
        httpManager.messageHttpManager.sendMessage(chatId, sendMessage)
    }

    /**
     * Use this method only if you have reusableInfo (id, fileId, photoToken) on Media, which you has already sent
     *
     * @param sendParams - look at this class for more information
     * @param reusableMediaParams - this class contains reusable info about media which had already uploaded
     *
     * @return - [ResultRequest] which contains Success with current response from server or Failure with [HttpException] or [Exception]
     */
    suspend fun sendMedia(sendParams: SendParams, reusableMediaParams: ReusableMediaParams): ResultRequest<PreparedMessage> {
        return sendForUserOrChat(sendParams.userId, sendParams.chatId, createSendMessageForReusableMedia(sendParams.sendMessage, reusableMediaParams))
    }

    /**
     * This method need for send text message for Chat by chatId
     * You should know that this method notify all users
     *
     * @param chatId - this is inline class [ChatId] which contains chatId
     * @param text - text of new message
     *
     * @return - [ResultRequest] which contains Success with current response from server or Failure with [HttpException] or [Exception]
     */
    suspend fun sendText(chatId: ChatId, text: String): ResultRequest<PreparedMessage> = send(chatId, RequestSendMessage(text))

    /**
     * @see [sendText]
     */
    suspend fun sendText(userId: UserId, text: String): ResultRequest<PreparedMessage> = send(userId, RequestSendMessage(text))

    /**
     * If you want send users in chat some notification that you now sending something in chat, you just need use this method.
     * @param chatId - inline class which contains chat id where you want send [Action]
     */
    suspend fun startTyping(chatId: ChatId) {
        typingController.startTyping(chatId)
    }

    /**
     * If you want send users in chat some notification that you stop sending something, you just need use this method.
     * @param chatId - inline class which contains chat id where you want send [Action]
     */
    fun stopTyping(chatId: ChatId) {
        typingController.stopTyping(chatId)
    }

    /**
     * This method need you if you want answer User with message for replace for old message with keyboard and/or if you want
     * answer User with Notification (Toast), on button click (on callback by Payload)
     *
     * @param callbackId - inline class which contains unique identifier of keyboard for specific message
     * @param requestAnswerCallback - this class contains message for replace for old message with keyboard, where user click on button.
     * Also contains userId and notification if you want send Toast for user
     *
     * @return you get [ResultRequest] class which may contains [ResultRequest.Success] or [ResultRequest.Failure],
     * success contains [Default] class which you say, that answer was success,
     * failure may contains [HttpException] with some http code and error body, or general [Exception]
     */
    suspend fun answer(callbackId: CallbackId, requestAnswerCallback: AnswerCallback): ResultRequest<Default> = startRequest {
        httpManager.answerOnCallback(callbackId, requestAnswerCallback)
    }

    /**
     * This method give you uploadUrl [Upload], which you need pass to [upload] method
     *
     * @param uploadType - this is enum which contains type of you upload object
     *
     * @return [ResultRequest] which contains [ResultRequest.Success] or [ResultRequest.Failure], [ResultRequest.Success] will contains
     * [Upload] with url for specific [UploadType], now you can use [upload] method for upload your media to Server
     */
    suspend fun getUploadUrl(uploadType: UploadType): ResultRequest<Upload> = startRequest {
        httpManager.getUploadUrl(uploadType)
    }

    /**
     * This method need if you want upload something with [UploadType] (for example, video, photo, audio or file)
     *
     * @param uploadParams - this class contains path of your local file (photo, video etc.) and [UploadType]
     * @param upload - this parameter you get if you call method [getUploadUrl]
     *
     * @return you get [ResultRequest] class which may contains [ResultRequest.Success] or [ResultRequest.Failure],
     * success contains [UploadInfo] class which contains photoToken or video id (audio), or file id,
     * failure may contains [HttpException] with some http code and error body, or general [Exception]
     */
    suspend fun upload(uploadParams: UploadParams, upload: Upload): ResultRequest<UploadInfo> = startRequest {
        httpManager.upload(upload.url, uploadParams.uploadType, uploadParams.path)
    }

    /**
     * This method need for send message with upload some Medias
     */
    internal suspend fun sendWithUpload(
        chatId: ChatId,
        userId: UserId,
        sendMessage: RequestSendMessage,
        upload: Upload,
        uploadParams: UploadParams
    ): ResultRequest<PreparedMessage> {
        val resultUpload = upload(uploadParams, upload)
        return when (resultUpload) {
            is ResultRequest.Success -> sendByUploadType(chatId, userId, uploadParams.uploadType, sendMessage, resultUpload)
            is ResultRequest.Failure -> ResultRequest.Failure(resultUpload.httpStatusCode, resultUpload.error, resultUpload.exception)
        }
    }

    /**
     * This method need for prepare SendMessage by upload type and send it by chatId or userId
     * @see [createSendMessageForImageToken] or [createSendMessageForMediaToken]
     */
    private suspend fun sendByUploadType(
        chatId: ChatId,
        userId: UserId,
        uploadType: UploadType,
        sendMessage: RequestSendMessage,
        resultUpload: ResultRequest.Success<UploadInfo>
    ): ResultRequest<PreparedMessage> = if (uploadType == UploadType.PHOTO) {
        val message = createSendMessageForImageToken(sendMessage, resultUpload.response)
        if (chatId.id == -1L) send(userId, message) else send(chatId, message)
    } else {
        val message = createSendMessageForMediaToken(uploadType, sendMessage, resultUpload.response)
        if (chatId.id == -1L) send(userId, message) else send(chatId, message)
    }

    /**
     * This method need for answer on callback with upload some Medias
     */
    internal suspend fun answerWithUpload(
        callbackId: CallbackId,
        sendMessage: RequestSendMessage,
        upload: Upload,
        uploadParams: UploadParams
    ): ResultRequest<Default> {
        val resultUpload = upload(uploadParams, upload)
        return when (resultUpload) {
            is ResultRequest.Success -> answerByUploadType(callbackId, uploadParams.uploadType, sendMessage, resultUpload)
            is ResultRequest.Failure -> ResultRequest.Failure(resultUpload.httpStatusCode, resultUpload.error, resultUpload.exception)
        }
    }

    /**
     * This method need for prepare AnswerCallback by upload type.
     * @see [createAnswerCallbackForImageToken] or [createAnswerCallbackForMediaToken]
     */
    private suspend fun answerByUploadType(
        callbackId: CallbackId,
        uploadType: UploadType,
        sendMessage: chat.tamtam.botsdk.model.request.SendMessage,
        resultUpload: ResultRequest.Success<UploadInfo>
    ): ResultRequest<Default> = if (uploadType == UploadType.PHOTO) {
        answer(callbackId, createAnswerCallbackForImageToken(sendMessage, resultUpload.response))
    } else {
        answer(callbackId, createAnswerCallbackForMediaToken(uploadType, sendMessage, resultUpload.response))
    }

    /**
     * General request method, which contains try catch for some exceptions when we call Http request.
     * @param request - this is Http request from [HttpManager] and his delegates [MessageHttpManager] and etc.
     */
    private inline fun <reified R, reified PR> startRequest(request: () -> HttpResult<R>): ResultRequest<PR> = try {
        when (val httpResult = request()) {
            is Success -> ResultRequest.Success(httpResult.response.map())
            is Failure -> {
                ResultRequest.Failure(httpResult.response.code(), parseError(httpResult.response), HttpException(httpResult.response))
            }
        }
    } catch (e: HttpException) {
        ResultRequest.Failure(e.code(), parseError(e.response()), e)
    } catch (e: Exception) {
        ResultRequest.Failure(-1, Error(code = "general", message = e.message?.let { it } ?: ""), e)
    }

    @UseExperimental(UnstableDefault::class)
    private fun <R> parseError(e: Response<R>): Error {
        return e.errorBody()?.let {
            Json.parse(Error.serializer(), it.string())
        } ?: Error(code = "general", message = e.message())
    }

    private fun checkCount(count: Int) {
        check(count in 0..100) {
            "Count must be from 0 to 100, current count $count"
        }
    }

    private suspend fun sendForUserOrChat(userId: UserId, chatId: ChatId, sendMessage: RequestSendMessage): ResultRequest<PreparedMessage> {
        check(chatId.id != -1L || userId.id != -1L) {
            "ChatId or UserId must be correct, current both are -1L"
        }
        return if (chatId.id == -1L) {
            send(userId, sendMessage)
        } else {
            send(chatId, sendMessage)
        }
    }

}

/**
 * This class give you some information about success or error request.
 * Success give you response which you want
 * Failure give you information about Http error or general error (Some IO exception or Timeout or something else)
 */
sealed class ResultRequest<out R> {
    /**
     * @param response - this is generic type which contains response
     */
    class Success<R>(val response: R) : ResultRequest<R>()
    /**
     * @param httpStatusCode - if exception about bad requests or server error is status code, if locale exception is [-1]
     * @param error - error contains code from server and message, or general code if exception is local
     * @param exception - this exception can contains specific [HttpException] or general [Exception]
     */
    class Failure<R>(val httpStatusCode: Int, val error: Error?, val exception: Exception) : ResultRequest<R>()
}