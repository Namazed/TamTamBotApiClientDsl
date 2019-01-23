package chat.tamtam.botsdk.client

import chat.tamtam.botsdk.model.CallbackId
import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.MessageId
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.response.BotInfo
import chat.tamtam.botsdk.model.response.Updates
import chat.tamtam.botsdk.model.response.SendMessage as ResponseSendMessage
import chat.tamtam.botsdk.model.request.SendMessage as RequestSendMessage
import chat.tamtam.botsdk.model.response.AnswerCallback as ResponseAnswerCallback
import chat.tamtam.botsdk.model.request.AnswerCallback as RequestAnswerCallback
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import okhttp3.logging.HttpLoggingInterceptor
import java.net.URL

class BotHttpManager(
    private val botApiEndpoint: String,
    internal val botToken: String,
    private val httpClient: HttpClient = initHttpClient()
) {

    suspend fun getBotInfo() = httpClient.get<BotInfo> {
        url(URL("$botApiEndpoint/me"))
        parameter("access_token", botToken)
    }

    suspend fun getUpdates() = httpClient.get<Updates> {
        url(URL("$botApiEndpoint/updates"))
        parameter("access_token", botToken)
    }

    suspend fun sendMessage(userId: UserId, sendMessage: RequestSendMessage) = httpClient.post<ResponseSendMessage> {
        url(URL("$botApiEndpoint/messages"))
        parameter("access_token", botToken)
        parameter("user_id", userId.id)
        contentType(ContentType.parse("application/json"))
        body = sendMessage
    }

    suspend fun sendMessage(chatId: ChatId) = httpClient.post<ResponseSendMessage> {
        url(URL("$botApiEndpoint/messages"))
        parameter("access_token", botToken)
        parameter("chat_id", chatId.id)
        contentType(ContentType.parse("application/json"))
    }

    suspend fun editMessage(messageId: MessageId, sendMessage: RequestSendMessage) =
        httpClient.put<ResponseAnswerCallback> {
            url(URL("$botApiEndpoint/messages"))
            parameter("access_token", botToken)
            parameter("message_id", messageId.id)
            contentType(ContentType.parse("application/json"))
            body = sendMessage
        }

    suspend fun answerOnCallback(callbackId: CallbackId, answerCallback: RequestAnswerCallback) =
        httpClient.post<ResponseAnswerCallback> {
            url(URL("$botApiEndpoint/answers"))
            parameter("access_token", botToken)
            parameter("callback_id", callbackId.id)
            contentType(ContentType.parse("application/json"))
            body = answerCallback
        }

}

private fun initHttpClient(): HttpClient = HttpClient(OkHttp) {
    engine {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        addInterceptor(loggingInterceptor)
        config {
            followRedirects(true)
        }
    }

    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}

