package chat.tamtam.botsdk.client

import chat.tamtam.botsdk.client.api.AnswerApi
import chat.tamtam.botsdk.client.api.BotApi
import chat.tamtam.botsdk.client.api.UploadApi
import chat.tamtam.botsdk.client.retrofit.Failure
import chat.tamtam.botsdk.client.retrofit.HttpResult
import chat.tamtam.botsdk.client.retrofit.RetrofitFactory
import chat.tamtam.botsdk.client.retrofit.Success
import chat.tamtam.botsdk.client.retrofit.await
import chat.tamtam.botsdk.model.CallbackId
import chat.tamtam.botsdk.model.request.UploadType
import chat.tamtam.botsdk.model.response.Default
import chat.tamtam.botsdk.model.response.Updates
import chat.tamtam.botsdk.model.response.Upload
import chat.tamtam.botsdk.model.response.UploadInfo
import chat.tamtam.botsdk.model.response.User
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import retrofit2.Retrofit
import java.io.File
import chat.tamtam.botsdk.model.request.AnswerCallback as RequestAnswerCallback
import chat.tamtam.botsdk.model.request.AnswerNotificationCallback as RequestAnswerNotificationCallback
import chat.tamtam.botsdk.model.request.SendMessage as RequestSendMessage
import chat.tamtam.botsdk.model.response.SendMessage as ResponseSendMessage

internal const val BOT_TOKEN_FIELD = "access_token"

internal val IMAGE_MEDIA_TYPE = MediaType.parse("image/*")
internal val VIDEO_MEDIA_TYPE = MediaType.parse("video/*")
internal val AUDIO_MEDIA_TYPE = MediaType.parse("audio/*")
internal val ALL_MEDIA_TYPE = MediaType.parse("all")

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

    suspend fun getBotInfo(): HttpResult<User> {
        return botService.getBotInfo(botToken).await()
    }

    suspend fun getUpdates(marker: Long?): Updates {
        val resultUpdates = subscriptionHttpManager.getUpdates(marker)
        when (resultUpdates) {
            is Success<Updates> -> return resultUpdates.response
            is Failure<Updates> -> throw HttpException(resultUpdates.response)
        }
    }

    suspend fun answerOnCallback(callbackId: CallbackId, answerCallback: RequestAnswerCallback): HttpResult<Default> =
        answerService.answerOnCallback(botToken, callbackId, answerCallback).await()

    suspend fun answerOnCallback(callbackId: CallbackId, answerCallback: RequestAnswerNotificationCallback): HttpResult<Default> =
        answerService.answerOnCallback(botToken, callbackId, answerCallback).await()

    suspend fun getUploadUrl(uploadType: UploadType): HttpResult<Upload> = uploadService.uploadUrl(botToken, uploadType).await()

    suspend fun upload(url: String, uploadType: UploadType, filePath: String): HttpResult<UploadInfo> {
        val file = File(filePath)
        return upload(url, uploadType, file.readBytes(), file.name)
    }

    suspend fun upload(url: String, uploadType: UploadType, byteArray: ByteArray, fileName: String): HttpResult<UploadInfo> {
        val mediaType = when (uploadType) {
            UploadType.PHOTO -> IMAGE_MEDIA_TYPE
            UploadType.VIDEO -> VIDEO_MEDIA_TYPE
            UploadType.AUDIO -> AUDIO_MEDIA_TYPE
            UploadType.FILE -> ALL_MEDIA_TYPE
        }
        val filePart = MultipartBody.Part.createFormData(
            "v1",
            fileName,
            RequestBody.create(mediaType, byteArray)
        )
        return uploadService.upload(url, filePart).await()
    }

}

