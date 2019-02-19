package chat.tamtam.botsdk.model.request

import chat.tamtam.botsdk.model.ImageUrl
import chat.tamtam.botsdk.model.VideoUrl
import chat.tamtam.botsdk.model.response.AttachType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SendMessage(
    val text: String,
    val attachments: List<Attachment> = emptyList(),
    @SerialName("notify") val notifyUser: Boolean = true
)

internal fun createSendMessageForKeyboard(sendMessage: SendMessage, keyboard: InlineKeyboard): SendMessage {
    return SendMessage(sendMessage.text, listOf(AttachmentKeyboard(AttachType.INLINE_KEYBOARD.value, keyboard)), sendMessage.notifyUser)
}

internal fun createSendMessageForImageUrl(sendMessage: SendMessage, imageUrl: ImageUrl): SendMessage {
    return SendMessage(sendMessage.text,
        listOf(AttachmentMediaWithUrl(AttachType.IMAGE.value, Payload(imageUrl.value))), sendMessage.notifyUser)
}

internal fun createSendMessageForVideoUrl(sendMessage: SendMessage, videoUrl: VideoUrl): SendMessage {
    return SendMessage(sendMessage.text,
        listOf(AttachmentMediaWithUrl(AttachType.IMAGE.value, Payload(videoUrl.value))), sendMessage.notifyUser)
}

internal fun createSendMessageForMediaToken(uploadType: UploadType, sendMessage: SendMessage, response: String): SendMessage {
    return when (uploadType) {
        UploadType.PHOTO -> SendMessage(sendMessage.text, listOf(AttachmentMedia(AttachType.IMAGE.value, response)), sendMessage.notifyUser)
        UploadType.VIDEO -> SendMessage(sendMessage.text, listOf(AttachmentMedia(AttachType.VIDEO.value, response)), sendMessage.notifyUser)
        UploadType.FILE -> SendMessage(sendMessage.text, listOf(AttachmentMedia(AttachType.FILE.value, response)), sendMessage.notifyUser)
        UploadType.AUDIO -> SendMessage(sendMessage.text, listOf(AttachmentMedia(AttachType.FILE.value, response)), sendMessage.notifyUser)
    }
}

internal fun createSendMessageForListImageTokens(sendMessage: SendMessage, uploadedTokens: List<String>): SendMessage {
    return SendMessage(sendMessage.text, uploadedTokens.map { AttachmentMedia(AttachType.IMAGE.value, it) }, sendMessage.notifyUser)
}
