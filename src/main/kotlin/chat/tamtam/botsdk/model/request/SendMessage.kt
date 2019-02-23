package chat.tamtam.botsdk.model.request

import chat.tamtam.botsdk.model.ImageUrl
import chat.tamtam.botsdk.model.VideoUrl
import chat.tamtam.botsdk.model.response.AttachType
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.list
import kotlinx.serialization.serializer
import kotlinx.serialization.withName

@Serializable
class SendMessage(
    val text: String,
    @Serializable(with = RequestAttachmentsSerializer::class) val attachments: List<Attachment> = emptyList(),
    @SerialName("notify") val notifyUser: Boolean = true
)

object RequestAttachmentsSerializer : KSerializer<List<Attachment>> {
    override val descriptor: SerialDescriptor
        get() = StringDescriptor.withName("RequestAttachments")

    @UseExperimental(ImplicitReflectionSerializer::class)
    override fun deserialize(input: Decoder): List<Attachment> {
        return Attachment::class.serializer().list.deserialize(input)
    }

    @UseExperimental(ImplicitReflectionSerializer::class)
    override fun serialize(output: Encoder, obj: List<Attachment>) {
        Attachment::class.serializer().list.serialize(output, obj)
    }
}

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
