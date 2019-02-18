package chat.tamtam.botsdk.client

import chat.tamtam.botsdk.model.CallbackId
import chat.tamtam.botsdk.model.request.UploadType
import chat.tamtam.botsdk.model.response.BotInfo
import chat.tamtam.botsdk.model.response.Default
import chat.tamtam.botsdk.model.response.Updates
import chat.tamtam.botsdk.model.response.Upload
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.content.PartData
import io.ktor.http.contentType
import kotlinx.io.streams.asInput
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.net.URL
import chat.tamtam.botsdk.model.request.AnswerCallback as RequestAnswerCallback
import chat.tamtam.botsdk.model.request.AnswerNotificationCallback as RequestAnswerNotificationCallback
import chat.tamtam.botsdk.model.request.SendMessage as RequestSendMessage
import chat.tamtam.botsdk.model.response.SendMessage as ResponseSendMessage

class BotHttpManager(
    private val botApiEndpoint: String,
    internal val botToken: String,
    private val httpClient: HttpClient = initHttpClient(),
    internal val chatApi: ChatApi = ChatApi("$botApiEndpoint/chats", botToken, httpClient),
    internal val messageApi: MessageApi = MessageApi("$botApiEndpoint/messages", botToken, httpClient),
    internal val subscriptionApi: SubscriptionApi = SubscriptionApi("$botApiEndpoint/subscriptions", botToken, httpClient)
) {

    suspend fun getBotInfo() = httpClient.get<BotInfo> {
        url(URL("$botApiEndpoint/me"))
        parameter("access_token", botToken)
    }

    suspend fun getUpdates() = httpClient.get<Updates> {
        url(URL("$botApiEndpoint/updates"))
        parameter("access_token", botToken)
    }

    suspend fun answerOnCallback(callbackId: CallbackId, answerCallback: RequestAnswerCallback) =
        httpClient.post<Default> {
            url(URL("$botApiEndpoint/answers"))
            parameter("access_token", botToken)
            parameter("callback_id", callbackId.id)
            contentType(ContentType.parse("application/json"))
            body = answerCallback
        }

    suspend fun answerOnCallback(callbackId: CallbackId, answerCallback: RequestAnswerNotificationCallback) =
        httpClient.post<Default> {
            url(URL("$botApiEndpoint/answers"))
            parameter("access_token", botToken)
            parameter("callback_id", callbackId.id)
            contentType(ContentType.parse("application/json"))
            body = answerCallback
        }

    suspend fun getUploadUrl(uploadType: UploadType) = httpClient.post<Upload> {
        url(URL("$botApiEndpoint/uploads"))
        parameter("access_token", botToken)
        parameter("type", uploadType.type)
    }

    suspend fun upload(url: String, filePath: String) = httpClient.post<String> {
        url(URL(url))
        body = MultiPartFormDataContent(formData {
            PartData.FileItem({
                File(filePath).inputStream().asInput()
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

