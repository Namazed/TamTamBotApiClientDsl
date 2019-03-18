package chat.tamtam.botsdk.client

import chat.tamtam.botsdk.client.api.MessageApi
import chat.tamtam.botsdk.client.retrofit.HttpResult
import chat.tamtam.botsdk.client.retrofit.await
import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.MessageId
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.response.Messages
import retrofit2.Retrofit
import chat.tamtam.botsdk.model.request.AnswerCallback as RequestAnswerCallback
import chat.tamtam.botsdk.model.request.SendMessage as RequestSendMessage
import chat.tamtam.botsdk.model.response.SendMessage as ResponseSendMessage

class MessageHttpManager internal constructor(
    private val botToken: String,
    retrofit: Retrofit,
    private val messageService: MessageApi = retrofit.create(MessageApi::class.java)
) {
    suspend fun getAllMessages(chatId: ChatId, fromTime: Long?, toTime: Long?, count: Int = 50): HttpResult<Messages> =
        messageService.getMessages(botToken, count, fromTime, toTime, chatId).await()

    suspend fun sendMessage(userId: UserId, sendMessage: RequestSendMessage): HttpResult<ResponseSendMessage> =
        messageService.sendMessage(botToken, userId, sendMessage).await()

    suspend fun sendMessage(chatId: ChatId, sendMessage: RequestSendMessage): HttpResult<ResponseSendMessage> =
        messageService.sendMessage(botToken, chatId, sendMessage).await()

    suspend fun editMessage(messageId: MessageId, sendMessage: RequestSendMessage): HttpResult<ResponseSendMessage> =
        messageService.editMessage(botToken, messageId, sendMessage).await()

}
