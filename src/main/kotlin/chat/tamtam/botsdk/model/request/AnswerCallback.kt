package chat.tamtam.botsdk.model.request

import chat.tamtam.botsdk.model.ImageUrl
import chat.tamtam.botsdk.model.VideoUrl
import chat.tamtam.botsdk.model.response.AttachType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class AnswerCallback(
    val message: SendMessage
)

@Serializable
class AnswerNotificationCallback(
    @SerialName("user_id") val userId: Long,
    val notification: String
)

internal fun createAnswerCallbackForKeyboard(sendMessage: SendMessage, keyboard: InlineKeyboard): AnswerCallback {
    return AnswerCallback(SendMessage(sendMessage.text, listOf(AttachmentKeyboard(AttachType.INLINE_KEYBOARD.value.toLowerCase(), keyboard)), sendMessage.notifyUser))
}

internal fun createAnswerCallbackForImageUrl(sendMessage: SendMessage, imageUrl: ImageUrl): AnswerCallback {
    return AnswerCallback(SendMessage(sendMessage.text,
        listOf(AttachmentMediaWithUrl(AttachType.IMAGE.value, Payload(imageUrl.value))), sendMessage.notifyUser))
}

internal fun createAnswerCallbackForVideoUrl(sendMessage: SendMessage, videoUrl: VideoUrl): AnswerCallback {
    return AnswerCallback(SendMessage(sendMessage.text,
        listOf(AttachmentMediaWithUrl(AttachType.IMAGE.value, Payload(videoUrl.value))), sendMessage.notifyUser))
}

internal fun createAnswerCallbackForMediaToken(uploadType: UploadType, sendMessage: SendMessage, response: String): AnswerCallback {
    return when (uploadType) {
        UploadType.PHOTO -> AnswerCallback(SendMessage(sendMessage.text, listOf(AttachmentMedia(AttachType.IMAGE.value, response)), sendMessage.notifyUser))
        UploadType.VIDEO -> AnswerCallback(SendMessage(sendMessage.text, listOf(AttachmentMedia(AttachType.VIDEO.value, response)), sendMessage.notifyUser))
        UploadType.FILE -> AnswerCallback(SendMessage(sendMessage.text, listOf(AttachmentMedia(AttachType.FILE.value, response)), sendMessage.notifyUser))
        UploadType.AUDIO -> AnswerCallback(SendMessage(sendMessage.text, listOf(AttachmentMedia(AttachType.FILE.value, response)), sendMessage.notifyUser))
    }
}

internal fun createAnswerCallbackForListImageTokens(sendMessage: SendMessage, uploadedTokens: List<String>): AnswerCallback {
    return AnswerCallback(SendMessage(sendMessage.text, uploadedTokens.map { AttachmentMedia(AttachType.IMAGE.value, it) }, sendMessage.notifyUser))
}