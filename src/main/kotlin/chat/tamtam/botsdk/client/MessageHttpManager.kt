package chat.tamtam.botsdk.client

import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.MessageId
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.response.Message
import com.namazed.orthobot.botsdk.client.api.MessageApi
import com.namazed.orthobot.botsdk.client.retrofit.await
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import chat.tamtam.botsdk.model.request.AnswerCallback as RequestAnswerCallback
import chat.tamtam.botsdk.model.request.SendMessage as RequestSendMessage
import chat.tamtam.botsdk.model.response.SendMessage as ResponseSendMessage

class MessageHttpManager internal constructor(
    private val botToken: String,
    retrofit: Retrofit,
    private val messageService: MessageApi = retrofit.create(MessageApi::class.java)
) {
    suspend fun getAllMessages(chatId: ChatId, fromTime: Long, toTime: Long, count: Int = 50): Response<List<Message>> =
        messageService.getMessages(botToken, count, fromTime, toTime, chatId).await()

    suspend fun sendMessage(userId: UserId, sendMessage: RequestSendMessage): ResponseSendMessage {
        val response = messageService.sendMessage(botToken, userId, sendMessage).await()
        return response.body() ?: throw HttpException(response)
    }

    suspend fun sendMessage(chatId: ChatId, sendMessage: RequestSendMessage): ResponseSendMessage {
        val response = messageService.sendMessage(botToken, chatId, sendMessage).await()
        return response.body() ?: throw HttpException(response)
    }

    suspend fun editMessage(messageId: MessageId, sendMessage: RequestSendMessage): ResponseSendMessage {
        val response = messageService.editMessage(botToken, messageId, sendMessage).await()
        return response.body() ?: throw HttpException(response)
    }

}
