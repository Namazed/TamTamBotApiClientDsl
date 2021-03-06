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
import chat.tamtam.botsdk.model.MediaTypes
import chat.tamtam.botsdk.model.request.UploadType
import chat.tamtam.botsdk.model.response.Bot
import chat.tamtam.botsdk.model.response.Default
import chat.tamtam.botsdk.model.response.Updates
import chat.tamtam.botsdk.model.response.Upload
import chat.tamtam.botsdk.model.response.UploadInfo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import retrofit2.Retrofit
import java.io.File
import chat.tamtam.botsdk.model.request.AnswerCallback as RequestAnswerCallback
import chat.tamtam.botsdk.model.request.Bot as RequestBot

internal const val BOT_TOKEN_FIELD = "access_token"
internal const val VERSION_FIELD = "v"

internal const val API_VERSION = "0.1.8"

//todo delete this layer, save only specific manager, or wrap in result in this layer
internal class HttpManager(
    internal val botToken: String,
    private val httpLogsEnabled: Boolean = false,
    retrofit: Retrofit = RetrofitFactory.createRetrofit(httpLogsEnabled = httpLogsEnabled),
    internal val messageHttpManager: MessageHttpManager = MessageHttpManager(botToken, API_VERSION, retrofit),
    internal val chatHttpManager: ChatHttpManager = ChatHttpManager(botToken, API_VERSION, retrofit),
    internal val subscriptionHttpManager: SubscriptionHttpManager = SubscriptionHttpManager(botToken, API_VERSION, retrofit),
    private val answerService: AnswerApi = retrofit.create(AnswerApi::class.java),
    private val uploadService: UploadApi = retrofit.create(UploadApi::class.java),
    private val botService: BotApi = retrofit.create(BotApi::class.java)
) {

    suspend fun getBotInfo(): HttpResult<Bot> {
        return botService.getBotInfo(botToken, API_VERSION).await()
    }

    suspend fun editBotInfo(botInfo: RequestBot): HttpResult<Bot> {
        return botService.editBotInfo(botToken, API_VERSION, botInfo).await()
    }

    suspend fun getUpdates(marker: Long?): Updates {
        when (val resultUpdates = subscriptionHttpManager.getUpdates(marker)) {
            is Success<Updates> -> return resultUpdates.response
            is Failure<Updates> -> throw HttpException(resultUpdates.response)
        }
    }

    suspend fun answerOnCallback(callbackId: CallbackId, answerCallback: RequestAnswerCallback): HttpResult<Default> =
        answerService.answerOnCallback(botToken, API_VERSION, callbackId, answerCallback).await()

    suspend fun getUploadUrl(uploadType: UploadType): HttpResult<Upload> = uploadService.uploadUrl(botToken, API_VERSION, uploadType).await()

    suspend fun upload(url: String, uploadType: UploadType, filePath: String): HttpResult<UploadInfo> {
        val file = File(filePath)
        return upload(url, uploadType, file.readBytes(), file.name)
    }

    suspend fun upload(url: String, uploadType: UploadType, byteArray: ByteArray, fileName: String): HttpResult<UploadInfo> {
        val mediaType = when (uploadType) {
            UploadType.PHOTO -> MediaTypes.IMAGE_MEDIA_TYPE.type
            UploadType.VIDEO -> MediaTypes.VIDEO_MEDIA_TYPE.type
            UploadType.AUDIO -> MediaTypes.AUDIO_MEDIA_TYPE.type
            UploadType.FILE -> MediaTypes.ALL_MEDIA_TYPE.type
        }
        val filePart = MultipartBody.Part.createFormData(
            "v1",
            fileName,
            RequestBody.create(mediaType, byteArray)
        )
        return uploadService.upload(url, filePart).await()
    }

}

