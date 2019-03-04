package chat.tamtam.botsdk.client

import chat.tamtam.botsdk.model.CallbackId
import chat.tamtam.botsdk.model.request.UploadType
import chat.tamtam.botsdk.model.response.BotInfo
import chat.tamtam.botsdk.model.response.Default
import chat.tamtam.botsdk.model.response.Updates
import chat.tamtam.botsdk.model.response.Upload
import chat.tamtam.botsdk.model.response.UploadInfo
import com.namazed.orthobot.botsdk.client.api.AnswerApi
import com.namazed.orthobot.botsdk.client.api.BotApi
import com.namazed.orthobot.botsdk.client.api.UploadApi
import com.namazed.orthobot.botsdk.client.retrofit.RetrofitFactory
import com.namazed.orthobot.botsdk.client.retrofit.await
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import chat.tamtam.botsdk.model.request.AnswerCallback as RequestAnswerCallback
import chat.tamtam.botsdk.model.request.AnswerNotificationCallback as RequestAnswerNotificationCallback
import chat.tamtam.botsdk.model.request.SendMessage as RequestSendMessage
import chat.tamtam.botsdk.model.response.SendMessage as ResponseSendMessage

internal const val BOT_TOKEN_FIELD = "access_token"

//todo delete this layer, save only specific manager, or wrap in result in this layer
internal class HttpManager(
    internal val botToken: String,
    retrofit: Retrofit = RetrofitFactory.createRetrofit(),
    internal val messageHttpManager: MessageHttpManager = MessageHttpManager(botToken, retrofit),
    internal val chatHttpManager: ChatHttpManager = ChatHttpManager(botToken, retrofit),
    internal val subscriptionHttpManager: SubscriptionHttpManager = SubscriptionHttpManager(botToken, retrofit),
    private val answerService: AnswerApi = retrofit.create(AnswerApi::class.java),
    private val uploadService: UploadApi = retrofit.create(UploadApi::class.java),
    private val botService: BotApi = retrofit.create(BotApi::class.java)
) {

    suspend fun getBotInfo(): Response<BotInfo> {
        return botService.getBotInfo(botToken).await()
    }

    suspend fun getUpdates(): Updates {
        val resultUpdates = subscriptionHttpManager.getUpdates()
        when (resultUpdates) {
            is ResultUpdates.Success -> return resultUpdates.updates
            is ResultUpdates.Failure -> throw HttpException(resultUpdates.response)
        }
    }

    suspend fun answerOnCallback(callbackId: CallbackId, answerCallback: RequestAnswerCallback): Default {
        val response = answerService.answerOnCallback(botToken, callbackId, answerCallback).await()
        return response.body() ?: throw HttpException(response)
    }

    suspend fun answerOnCallback(callbackId: CallbackId, answerCallback: RequestAnswerNotificationCallback): Default {
        val response = answerService.answerOnCallback(botToken, callbackId, answerCallback).await()
        return response.body() ?: throw HttpException(response)
    }

    suspend fun getUploadUrl(uploadType: UploadType): Upload {
        return uploadService.uploadUrl(botToken, uploadType).await().body()!!
    }

    suspend fun upload(url: String, uploadType: UploadType, filePath: String): UploadInfo {
        val file = File(filePath)
        return upload(url, uploadType, file.readBytes(), file.name)
    }

    suspend fun upload(url: String, uploadType: UploadType, byteArray: ByteArray, fileName: String): UploadInfo {
        val mediaType = when (uploadType) {
            UploadType.PHOTO -> MediaType.parse("image/*")
            UploadType.VIDEO -> MediaType.parse("video/*")
            UploadType.AUDIO -> MediaType.parse("audio/*")
            UploadType.FILE -> MediaType.parse("all")
        }
        val filePart = MultipartBody.Part.createFormData(
            "v1",
            fileName,
            RequestBody.create(mediaType, byteArray)
        )
        val response = uploadService.upload(url, filePart).await()
        return response.body() ?: throw HttpException(response)
    }

}

