package chat.tamtam.botsdk.model.request

import chat.tamtam.botsdk.model.AttachType
import chat.tamtam.botsdk.model.ImageUrl
import chat.tamtam.botsdk.model.VideoUrl
import chat.tamtam.botsdk.model.response.UploadInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This class need use when you want send message.
 *
 * @param text - text content of message
 * @param attachments - list of attaches, for example [AttachmentKeyboard], [AttachmentLocation], [AttachmentMediaWithId] and etc.
 * @param notifyUser - this flag use in chat, when you want send quiet message or not.
 */
@Serializable
class SendMessage(
    val text: String,
    @Serializable() val attachments: List<Attachment> = emptyList(),
    @SerialName("notify") val notifyUser: Boolean = true
)

internal fun createSendMessageForKeyboard(sendMessage: SendMessage, keyboard: InlineKeyboard): SendMessage {
    return SendMessage(sendMessage.text, listOf(AttachmentKeyboard(AttachType.INLINE_KEYBOARD.value, keyboard)), sendMessage.notifyUser)
}

internal fun createSendMessageForImageUrl(sendMessage: SendMessage, imageUrl: ImageUrl): SendMessage {
    return SendMessage(sendMessage.text,
        listOf(AttachmentMediaWithUrl(AttachType.IMAGE.value, PayloadUrl(imageUrl.value))), sendMessage.notifyUser)
}

internal fun createSendMessageForVideoUrl(sendMessage: SendMessage, videoUrl: VideoUrl): SendMessage {
    return SendMessage(sendMessage.text,
        listOf(AttachmentMediaWithUrl(AttachType.IMAGE.value, PayloadUrl(videoUrl.value))), sendMessage.notifyUser)
}

internal fun createSendMessageForMediaToken(uploadType: UploadType, sendMessage: SendMessage, response: UploadInfo): SendMessage {
    return when (uploadType) {
        UploadType.VIDEO -> SendMessage(sendMessage.text, listOf(AttachmentMediaWithId(AttachType.VIDEO.value, response)), sendMessage.notifyUser)
        UploadType.FILE -> SendMessage(sendMessage.text, listOf(AttachmentMediaWithId(AttachType.FILE.value, response)), sendMessage.notifyUser)
        UploadType.AUDIO -> SendMessage(sendMessage.text, listOf(AttachmentMediaWithId(AttachType.FILE.value, response)), sendMessage.notifyUser)
        else -> throw IllegalArgumentException("Incorrect uploadType for this method")
    }
}

internal fun createSendMessageForImageToken(sendMessage: SendMessage, response: UploadInfo): SendMessage {
    return SendMessage(sendMessage.text, listOf(AttachmentPhoto(AttachType.IMAGE.value, response)), sendMessage.notifyUser)
}

internal fun createSendMessageForListImageTokens(sendMessage: SendMessage, uploadedTokens: List<UploadInfo>): SendMessage {
    return SendMessage(sendMessage.text, uploadedTokens.map { AttachmentPhoto(AttachType.IMAGE.value, it) }, sendMessage.notifyUser)
}
