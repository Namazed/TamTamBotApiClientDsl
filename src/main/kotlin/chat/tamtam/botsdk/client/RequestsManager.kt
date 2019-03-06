package chat.tamtam.botsdk.client

import chat.tamtam.botsdk.client.retrofit.Failure
import chat.tamtam.botsdk.client.retrofit.Success
import chat.tamtam.botsdk.model.CallbackId
import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.request.*
import chat.tamtam.botsdk.model.response.BotInfo
import chat.tamtam.botsdk.model.response.Default
import chat.tamtam.botsdk.model.response.Upload
import chat.tamtam.botsdk.model.response.UploadInfo
import chat.tamtam.botsdk.typing.TypingController
import retrofit2.HttpException
import chat.tamtam.botsdk.model.request.SendMessage as RequestSendMessage
import chat.tamtam.botsdk.model.response.SendMessage as ResponseSendMessage

class RequestsManager internal constructor(
    private val httpManager: HttpManager,
    private val typingController: TypingController
) {

    suspend fun getBotInfo(): BotInfo {
        val httpResult = httpManager.getBotInfo()
        return when (httpResult) {
            is Success -> httpResult.response
            is Failure -> TODO()
        }
    }

    /**
     * This method need for send [RequestSendMessage] for User by userId
     *
     * @param userId - this is inline class [UserId] which contains userId
     * @param sendMessage - this is [RequestSendMessage] which you send for User
     * @return - [ResultSend] which contains Success with current response from server or Failure with [HttpException] or [Exception]
     */
    suspend fun send(userId: UserId, sendMessage: RequestSendMessage): ResultSend {
        return try {
            val httpResult = httpManager.messageHttpManager.sendMessage(userId, sendMessage)
            // todo объединить в один Result с answer и upload
            when (httpResult) {
                is Success -> ResultSend.Success(httpResult.response)
                is Failure -> ResultSend.Failure(HttpException(httpResult.response))
            }
        } catch (e: HttpException) {
            ResultSend.Failure(e)
        } catch (e: Exception) {
            ResultSend.Failure(e)
        }
    }

    /**
     * Look on [send]
     */
    suspend fun send(chatId: ChatId, sendMessage: RequestSendMessage): ResultSend {
        return try {
            val httpResult = httpManager.messageHttpManager.sendMessage(chatId, sendMessage)
            when (httpResult) {
                is Success -> ResultSend.Success(httpResult.response)
                is Failure -> ResultSend.Failure(HttpException(httpResult.response))
            }
        } catch (e: HttpException) {
            ResultSend.Failure(e)
        } catch (e: Exception) {
            ResultSend.Failure(e)
        }
    }

    /**
     * This method need for send text message for Chat by chatId
     * You should know that this method notify all users
     *
     * @param chatId - this is inline class [ChatId] which contains chatId
     * @param text - text of new message
     * @return - [ResultSend] which contains Success with current response from server or Failure with [HttpException] or [Exception]
     */
    suspend fun sendText(chatId: ChatId, text: String): ResultSend = send(chatId, RequestSendMessage(text))

    /**
     * Look on [sendText]
     */
    suspend fun sendText(userId: UserId, text: String): ResultSend = send(userId, RequestSendMessage(text))

    suspend fun startTyping(chatId: ChatId) {
        typingController.startTyping(chatId)
    }

    fun stopTyping(chatId: ChatId) {
        typingController.stopTyping(chatId)
    }

    suspend fun answer(callbackId: CallbackId, requestAnswerCallback: AnswerCallback): ResultAnswer {
        return try {
            val httpResult = httpManager.answerOnCallback(callbackId, requestAnswerCallback)
            when (httpResult) {
                is Success -> ResultAnswer.Success(httpResult.response)
                is Failure -> ResultAnswer.Failure(HttpException(httpResult.response))
            }
        } catch (e: HttpException) {
            ResultAnswer.Failure(e)
        } catch (e: Exception) {
            ResultAnswer.Failure(e)
        }
    }

    /**
     * This method need you if you want answer User with Notification (Toast) on button click (on callback by Payload)
     *
     * @param callbackId - this is inline class [CallbackId] which contains keyboard id
     * @param requestAnswerNotificationCallback - this parameter you get if you call method [getUploadUrl]
     * @return [ResultAnswer] which contains [ResultAnswer.Success] or [ResultAnswer.Failure]
     */
    suspend fun answer(
        callbackId: CallbackId,
        requestAnswerNotificationCallback: AnswerNotificationCallback
    ): ResultAnswer {
        return try {
            val httpResult = httpManager.answerOnCallback(callbackId, requestAnswerNotificationCallback)
            when (httpResult) {
                is Success -> ResultAnswer.Success(httpResult.response)
                is Failure -> ResultAnswer.Failure(HttpException(httpResult.response))
            }
        } catch (e: HttpException) {
            ResultAnswer.Failure(e)
        } catch (e: Exception) {
            ResultAnswer.Failure(e)
        }
    }

    /**
     * This method give you uploadUrl [Upload], which you need pass to [upload] method
     *
     * @param uploadParams - this class contains path of your local file (photo, video etc.) and [UploadType]
     * @param upload - this parameter you get if you call method [getUploadUrl]
     * @return [ResultUploadUrl] which contains [ResultUploadUrl.Success] or [ResultUploadUrl.Failure], [ResultUploadUrl.Success] will contains
     * [Upload] with url for specific [UploadType], now you can use [upload] method for upload your media to Server
     */
    suspend fun getUploadUrl(uploadType: UploadType): ResultUploadUrl {
        return try {
            val httpResult = httpManager.getUploadUrl(uploadType)
            when (httpResult) {
                is Success -> ResultUploadUrl.Success(httpResult.response)
                is Failure -> ResultUploadUrl.Failure(HttpException(httpResult.response))
            }
        } catch (e: HttpException) {
            ResultUploadUrl.Failure(e)
        } catch (e: Exception) {
            ResultUploadUrl.Failure(e)
        }
    }

    /**
     * This method need if you want upload something with [UploadType] (for example, video, photo, audio or file)
     *
     * @param uploadParams - this class contains path of your local file (photo, video etc.) and [UploadType]
     * @param upload - this parameter you get if you call method [getUploadUrl]
     * @return [ResultUpload] which contains [ResultUpload.Success] or [ResultUpload.Failure], [ResultUpload.Success] will contains
     * response for specific [UploadType] ([String] with photoTokens for [UploadType.PHOTO] or id [Long] for file, video, audio)
     */
    suspend fun upload(uploadParams: UploadParams, upload: Upload): ResultUpload<UploadInfo> = try {
        val httpResult = httpManager.upload(upload.url, uploadParams.uploadType, uploadParams.path)
        when (httpResult) {
            is Success -> ResultUpload.Success(httpResult.response)
            is Failure -> ResultUpload.Failure(HttpException(httpResult.response))
        }
    } catch (e: HttpException) {
        ResultUpload.Failure(e)
    } catch (e: Exception) {
        ResultUpload.Failure(e)
    }

    internal suspend fun sendWithUpload(chatId: ChatId, userId: UserId, sendMessage: RequestSendMessage, upload: Upload, uploadParams: UploadParams): ResultSend {
        val resultUpload = upload(uploadParams, upload)
        return when (resultUpload) {
            is ResultUpload.Success<UploadInfo> -> sendByUploadType(chatId, userId, uploadParams.uploadType, sendMessage, resultUpload)
            is ResultUpload.Failure<UploadInfo> -> ResultSend.Failure(resultUpload.exception)
        }
    }

    private suspend fun sendByUploadType(
        chatId: ChatId,
        userId: UserId,
        uploadType: UploadType,
        sendMessage: chat.tamtam.botsdk.model.request.SendMessage,
        resultUpload: ResultUpload.Success<UploadInfo>
    ): ResultSend = if (uploadType == UploadType.PHOTO) {
        val message = createSendMessageForImageToken(sendMessage, resultUpload.response)
        if (chatId.id == -1L) send(userId, message) else send(chatId, message)
    } else {
        val message = createSendMessageForMediaToken(uploadType, sendMessage, resultUpload.response)
        if (chatId.id == -1L) send(userId, message) else send(chatId, message)
    }

    internal suspend fun answerWithUpload(callbackId: CallbackId, sendMessage: RequestSendMessage, upload: Upload, uploadParams: UploadParams): ResultAnswer {
        val resultUpload = upload(uploadParams, upload)
        return when (resultUpload) {
            is ResultUpload.Success<UploadInfo> -> answerByUploadType(callbackId, uploadParams.uploadType, sendMessage, resultUpload)
            is ResultUpload.Failure<UploadInfo> -> ResultAnswer.Failure(resultUpload.exception)
        }
    }

    private suspend fun answerByUploadType(
        callbackId: CallbackId,
        uploadType: UploadType,
        sendMessage: chat.tamtam.botsdk.model.request.SendMessage,
        resultUpload: ResultUpload.Success<UploadInfo>
    ): ResultAnswer = if (uploadType == UploadType.PHOTO) {
        answer(callbackId, createAnswerCallbackForImageToken(sendMessage, resultUpload.response))
    } else {
        answer(callbackId, createAnswerCallbackForMediaToken(uploadType, sendMessage, resultUpload.response))
    }

}

class PreparedAnswer(
    val answerCallback: AnswerCallback,
    val answerParams: AnswerParams
)

sealed class ResultSend {
    /**
     * @param response - this is [ResponseSendMessage] which contains chatId where message sent, created messageId,
     * recipientId in most cases equals chatId
     */
    class Success(val response: ResponseSendMessage) : ResultSend()
    /**
     * @param exception - this exception can contains specific [HttpException] or general [Exception]
     */
    class Failure(val exception: Exception) : ResultSend()
}

sealed class ResultAnswer {
    /**
     * @param response - this is status true or false
     */
    class Success(val response: Default) : ResultAnswer()
    /**
     * @param exception - this exception can contains specific [HttpException] or general [Exception]
     */
    class Failure(val exception: Exception) : ResultAnswer()
}

sealed class ResultUploadUrl {
    /**
     * @param response - this is [Upload] which contains url for upload your media to Server
     */
    class Success(val response: Upload) : ResultUploadUrl()
    /**
     * @param exception - this exception can contains specific [HttpException] or general [Exception]
     */
    class Failure(val exception: Exception) : ResultUploadUrl()
}

/**
 * This class return when you upload something by uploadUrl from [ResultUploadUrl]
 */
sealed class ResultUpload<out Response> {
    /**
     * @param response - for uploadType [UploadType.PHOTO] is [String] type, for other [UploadType] is [Long] type
     */
    class Success<Response>(val response: Response) : ResultUpload<Response>()
    /**
     * @param exception - this exception can contains specific [HttpException] or general [Exception]
     */
    class Failure<Response>(val exception: Exception) : ResultUpload<Response>()
}