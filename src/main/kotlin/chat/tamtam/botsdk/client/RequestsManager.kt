package chat.tamtam.botsdk.client

import chat.tamtam.botsdk.model.CallbackId
import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.request.AnswerCallback
import chat.tamtam.botsdk.model.request.AnswerNotificationCallback
import chat.tamtam.botsdk.model.request.AnswerParams
import chat.tamtam.botsdk.model.request.UploadParams
import chat.tamtam.botsdk.model.request.UploadType
import chat.tamtam.botsdk.model.request.createAnswerCallbackForMediaToken
import chat.tamtam.botsdk.model.request.createSendMessageForMediaToken
import chat.tamtam.botsdk.model.response.Default
import chat.tamtam.botsdk.model.response.Upload
import chat.tamtam.botsdk.typing.TypingController
import io.ktor.client.features.BadResponseStatusException
import chat.tamtam.botsdk.model.request.SendMessage as RequestSendMessage
import chat.tamtam.botsdk.model.response.SendMessage as ResponseSendMessage

class RequestsManager(
    private val botHttpManager: BotHttpManager,
    private val typingController: TypingController
) {

    /**
     * This method need for send [RequestSendMessage] for User by userId
     *
     * @param userId - this is inline class [UserId] which contains userId
     * @param sendMessage - this is [RequestSendMessage] which you send for User
     * @return - [ResultSend] which contains Success with current response from server or Failure with [BadResponseStatusException] or [Exception]
     */
    suspend fun send(userId: UserId, sendMessage: RequestSendMessage): ResultSend {
        return try {
            val response = botHttpManager.messageApi.sendMessage(userId, sendMessage)
            ResultSend.Success(response)
        } catch (e: BadResponseStatusException) {
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
            val response = botHttpManager.messageApi.sendMessage(chatId, sendMessage)
            ResultSend.Success(response)
        } catch (e: BadResponseStatusException) {
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
     * @return - [ResultSend] which contains Success with current response from server or Failure with [BadResponseStatusException] or [Exception]
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
            val response = botHttpManager.answerOnCallback(callbackId, requestAnswerCallback)
            ResultAnswer.Success(response)
        } catch (e: BadResponseStatusException) {
            ResultAnswer.Failure(e)
        } catch (e: Exception) {
            ResultAnswer.Failure(e)
        }
    }

    suspend fun answer(
        callbackId: CallbackId,
        requestAnswerNotificationCallback: AnswerNotificationCallback
    ): ResultAnswer {
        return try {
            val response = botHttpManager.answerOnCallback(callbackId, requestAnswerNotificationCallback)
            ResultAnswer.Success(response)
        } catch (e: BadResponseStatusException) {
            ResultAnswer.Failure(e)
        } catch (e: Exception) {
            ResultAnswer.Failure(e)
        }
    }

    suspend fun getUploadUrl(uploadType: UploadType): ResultUploadUrl {
        return try {
            val response = botHttpManager.getUploadUrl(uploadType)
            ResultUploadUrl.Success(response)
        } catch (e: BadResponseStatusException) {
            ResultUploadUrl.Failure(e)
        } catch (e: Exception) {
            ResultUploadUrl.Failure(e)
        }
    }

    suspend fun uploadLocalFile(upload: Upload, path: String): ResultUpload {
        return try {
            val response = botHttpManager.upload(upload.url, path)
            ResultUpload.Success(response)
        } catch (e: BadResponseStatusException) {
            ResultUpload.Failure(e)
        } catch (e: Exception) {
            ResultUpload.Failure(e)
        }
    }

    internal suspend fun sendWithUpload(chatId: ChatId, sendMessage: RequestSendMessage, upload: Upload, uploadParams: UploadParams): ResultSend {
        val resultUpload = uploadLocalFile(upload, uploadParams.path)
        return when (resultUpload) {
            is ResultUpload.Success -> send(chatId, createSendMessageForMediaToken(uploadParams.uploadType, sendMessage, resultUpload.response))
            is ResultUpload.Failure -> ResultSend.Failure(resultUpload.exception)
        }
    }

    internal suspend fun answerWithUpload(callbackId: CallbackId, sendMessage: RequestSendMessage, upload: Upload, uploadParams: UploadParams): ResultAnswer {
        val resultUpload = uploadLocalFile(upload, uploadParams.path)
        return when (resultUpload) {
            is ResultUpload.Success -> answer(callbackId, createAnswerCallbackForMediaToken(uploadParams.uploadType, sendMessage, resultUpload.response))
            is ResultUpload.Failure -> ResultAnswer.Failure(resultUpload.exception)
        }
    }
}

class PreparedAnswer(
    val answerCallback: AnswerCallback,
    val answerParams: AnswerParams
)

sealed class ResultSend {
    class Success(val response: ResponseSendMessage) : ResultSend()
    class Failure(val exception: Exception) : ResultSend()
}

sealed class ResultAnswer {
    class Success(val response: Default) : ResultAnswer()
    class Failure(val exception: Exception) : ResultAnswer()
}

sealed class ResultUploadUrl {
    class Success(val response: Upload) : ResultUploadUrl()
    class Failure(val exception: Exception) : ResultUploadUrl()
}

sealed class ResultUpload {
    class Success(val response: String) : ResultUpload()
    class Failure(val exception: Exception) : ResultUpload()
}