package chat.tamtam.botsdk.client

import chat.tamtam.botsdk.model.CallbackId
import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.MessageId
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.request.Action
import chat.tamtam.botsdk.model.request.UploadType
import chat.tamtam.botsdk.model.response.*
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.*
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.content.PartData
import io.ktor.http.contentType
import kotlinx.io.streams.asInput
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.net.URL
import chat.tamtam.botsdk.model.request.AnswerCallback as RequestAnswerCallback
import chat.tamtam.botsdk.model.request.SendMessage as RequestSendMessage
import chat.tamtam.botsdk.model.response.SendMessage as ResponseSendMessage

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

    suspend fun getAllChats(count: Int = 50, marker: Long? = null) = httpClient.get<ChatsResult> {
        url(URL("$botApiEndpoint/chats"))
        parameter("access_token", botToken)
        parameter("count", count)
        parameter("marker", marker)
    }

    suspend fun getAllMessages(chatId: ChatId, fromTime: Long, toTime: Long, count: Int = 50) = httpClient.get<List<Message>> {
        url(URL("$botApiEndpoint/chats"))
        parameter("access_token", botToken)
        parameter("chat_id", chatId.id)
        parameter("from", fromTime)
        parameter("to", toTime)
        parameter("count", count)
    }

    suspend fun sendAction(chatId: ChatId, action: Action) = httpClient.post<Default> {
        url(URL("$botApiEndpoint/chats/${chatId.id}/actions"))
        parameter("access_token", botToken)
        parameter("chat_id", chatId.id)
        parameter("action", action.name.toLowerCase())
    }

    suspend fun sendMessage(userId: UserId, sendMessage: RequestSendMessage) = httpClient.post<ResponseSendMessage> {
        url(URL("$botApiEndpoint/messages"))
        parameter("access_token", botToken)
        parameter("user_id", userId.id)
        contentType(ContentType.parse("application/json"))
        body = sendMessage
    }

    suspend fun sendMessage(chatId: ChatId, sendMessage: RequestSendMessage) = httpClient.post<ResponseSendMessage> {
        url(URL("$botApiEndpoint/messages"))
        parameter("access_token", botToken)
        parameter("chat_id", chatId.id)
        contentType(ContentType.parse("application/json"))
        body = sendMessage
    }

    suspend fun editMessage(messageId: MessageId, sendMessage: RequestSendMessage) =
        httpClient.put<Default> {
            url(URL("$botApiEndpoint/messages"))
            parameter("access_token", botToken)
            parameter("message_id", messageId.id)
            contentType(ContentType.parse("application/json"))
            body = sendMessage
        }

    suspend fun answerOnCallback(callbackId: CallbackId, answerCallback: RequestAnswerCallback) =
        httpClient.post<Default> {
            url(URL("$botApiEndpoint/answers"))
            parameter("access_token", botToken)
            parameter("callback_id", callbackId.id)
            contentType(ContentType.parse("application/json"))
            body = answerCallback
        }

    suspend fun uploadUrl(uploadType: UploadType) = httpClient.post<Upload> {
        url(URL("$botApiEndpoint/uploads"))
        parameter("access_token", botToken)
        parameter("type", uploadType.type)
    }

    suspend fun upload(url: String, filePath: String) = httpClient.post<String> {
        url(URL(url))
        body = MultiPartFormDataContent(formData {
            PartData.FileItem({
                File("/2").inputStream().asInput()
            }, {}, Headers.Empty)
        })
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

