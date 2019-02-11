package chat.tamtam.botsdk.client

import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.MessageId
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.response.Default
import chat.tamtam.botsdk.model.response.Message
import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import java.net.URL
import chat.tamtam.botsdk.model.request.AnswerCallback as RequestAnswerCallback
import chat.tamtam.botsdk.model.request.SendMessage as RequestSendMessage
import chat.tamtam.botsdk.model.response.SendMessage as ResponseSendMessage

class MessageApi(
    private val messageApiEndpoint: String,
    private val botToken: String,
    private val httpClient: HttpClient
) {
    suspend fun getAllMessages(chatId: ChatId, fromTime: Long, toTime: Long, count: Int = 50) =
        httpClient.get<List<Message>> {
            url(URL(messageApiEndpoint))
            parameter("access_token", botToken)
            parameter("chat_id", chatId.id)
            parameter("from", fromTime)
            parameter("to", toTime)
            parameter("count", count)
        }

    suspend fun sendMessage(userId: UserId, sendMessage: RequestSendMessage) = httpClient.post<ResponseSendMessage> {
        url(URL(messageApiEndpoint))
        parameter("access_token", botToken)
        parameter("user_id", userId.id)
        contentType(ContentType.parse("application/json"))
        body = sendMessage
    }

    suspend fun sendMessage(chatId: ChatId, sendMessage: RequestSendMessage) = httpClient.post<ResponseSendMessage> {
        url(URL(messageApiEndpoint))
        parameter("access_token", botToken)
        parameter("chat_id", chatId.id)
        contentType(ContentType.parse("application/json"))
        body = sendMessage
    }

    suspend fun editMessage(messageId: MessageId, sendMessage: RequestSendMessage) = httpClient.put<Default> {
        url(URL(messageApiEndpoint))
        parameter("access_token", botToken)
        parameter("message_id", messageId.id)
        contentType(ContentType.parse("application/json"))
        body = sendMessage
    }
}
