package chat.tamtam.botsdk.client

import chat.tamtam.botsdk.model.CallbackId
import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.request.AnswerCallback
import chat.tamtam.botsdk.model.request.AnswerNotificationCallback
import chat.tamtam.botsdk.model.request.AnswerParams
import chat.tamtam.botsdk.model.request.SendMessage
import chat.tamtam.botsdk.model.response.Default
import chat.tamtam.botsdk.typing.TypingController
import io.ktor.client.features.BadResponseStatusException

class RequestsManager(
    private val botHttpManager: BotHttpManager,
    private val typingController: TypingController
) {

    suspend fun send(userId: UserId, sendMessage: SendMessage): ResultSend {
        return try {
            val response = botHttpManager.messageApi.sendMessage(userId, sendMessage)
            ResultSend.Success(response)
        } catch (e: BadResponseStatusException) {
            ResultSend.Failure(e)
        } catch (e: Exception) {
            ResultSend.Failure(e)
        }
    }

    suspend fun send(chatId: ChatId, sendMessage: SendMessage): ResultSend {
        return try {
            val response = botHttpManager.messageApi.sendMessage(chatId, sendMessage)
            ResultSend.Success(response)
        } catch (e: BadResponseStatusException) {
            ResultSend.Failure(e)
        } catch (e: Exception) {
            ResultSend.Failure(e)
        }
    }

    suspend fun sendText(chatId: ChatId, text: String): ResultSend = send(chatId, SendMessage(text))

    suspend fun sendText(userId: UserId, text: String): ResultSend = send(userId, SendMessage(text))

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

    suspend fun answer(callbackId: CallbackId, requestAnswerNotificationCallback: AnswerNotificationCallback): ResultAnswer {
        return try {
            val response = botHttpManager.answerOnCallback(callbackId, requestAnswerNotificationCallback)
            ResultAnswer.Success(response)
        } catch (e: BadResponseStatusException) {
            ResultAnswer.Failure(e)
        } catch (e: Exception) {
            ResultAnswer.Failure(e)
        }
    }
}

class PreparedAnswer(
    val answerCallback: AnswerCallback,
    val answerParams: AnswerParams
)

sealed class ResultSend {
    class Success(val response: chat.tamtam.botsdk.model.response.SendMessage) : ResultSend()
    class Failure(val exception: Exception) : ResultSend()
}

sealed class ResultAnswer {
    class Success(val response: Default) : ResultAnswer()
    class Failure(val exception: Exception) : ResultAnswer()
}