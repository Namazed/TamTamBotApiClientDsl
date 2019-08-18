package chat.tamtam.botsdk.model.request

import chat.tamtam.botsdk.model.AttachType
import chat.tamtam.botsdk.model.ImageUrl
import chat.tamtam.botsdk.model.response.UploadInfo
import kotlinx.serialization.CompositeEncoder
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.internal.SerialClassDescImpl
import kotlinx.serialization.list

/**
 * This class need use when you want send message.
 *
 * @param text - text content of message
 * @param attachments - list of attaches, for example [AttachmentKeyboard], [AttachmentLocation], [AttachmentMediaWithUploadData] and etc.
 * @param notifyUser - this flag use in chat, when you want send quiet message or not.
 */
@Serializable
class SendMessage(
    val text: String? = null,
    val attachments: List<AttachmentContract>? = null,
    val notifyUser: Boolean = true,
    val link: LinkOnMessage? = null
) {
    @Serializer(forClass = SendMessage::class)
    companion object : KSerializer<SendMessage> {
        override val descriptor: SerialDescriptor = object : SerialClassDescImpl("SendMessage") {
            init {
                addElement("text", true)
                addElement("attachments", true)
                addElement("notify", true)
                addElement("link", true)
            }
        }

        override fun deserialize(decoder: Decoder): SendMessage {
            return SendMessage()
        }

        override fun serialize(encoder: Encoder, obj: SendMessage) {
            val compositeOutput: CompositeEncoder = encoder.beginStructure(descriptor)
            obj.text?.let {
                compositeOutput.encodeStringElement(descriptor, 0, it)
            }
            obj.attachments?.let {
                compositeOutput.encodeSerializableElement(descriptor, 1, AttachmentContract.serializer().list, it)
            }
            compositeOutput.encodeBooleanElement(descriptor, 2, obj.notifyUser)
            obj.link?.let {
                compositeOutput.encodeSerializableElement(descriptor, 3, LinkOnMessageSerializer, it)
            }
            compositeOutput.endStructure(descriptor)
        }
    }
}

internal fun createSendMessageForImageUrl(sendMessage: SendMessage, imageUrl: ImageUrl): SendMessage {
    return SendMessage(sendMessage.text,
        listOf(AttachmentPhotoWithUrl(AttachType.IMAGE.value, PayloadUrl(imageUrl.value))), sendMessage.notifyUser)
}

internal fun createSendMessageForMediaToken(uploadType: UploadType, sendMessage: SendMessage, response: UploadInfo): SendMessage {
    return when (uploadType) {
        UploadType.VIDEO -> SendMessage(sendMessage.text, listOf(AttachmentMediaWithUploadData(AttachType.VIDEO.value, response)), sendMessage.notifyUser)
        UploadType.FILE -> SendMessage(sendMessage.text, listOf(AttachmentMediaWithUploadData(AttachType.FILE.value, response)), sendMessage.notifyUser)
        UploadType.AUDIO -> SendMessage(sendMessage.text, listOf(AttachmentMediaWithUploadData(AttachType.FILE.value, response)), sendMessage.notifyUser)
        else -> throw IllegalArgumentException("Incorrect uploadType for this method")
    }
}

internal fun createSendMessageForReusableMedia(sendMessage: SendMessage, reusableMediaParams: ReusableMediaParams): SendMessage {
    val attachments = listOf(when {
        reusableMediaParams.token == null -> throw IllegalArgumentException("Token is null")
        reusableMediaParams.uploadType == UploadType.PHOTO ->
            AttachmentPhotoWithToken(AttachType.IMAGE.value, PayloadToken(reusableMediaParams.token))
        reusableMediaParams.uploadType == UploadType.VIDEO ->
            AttachmentMediaWithUploadData(AttachType.VIDEO.value, UploadInfo(token = reusableMediaParams.token))
        reusableMediaParams.uploadType == UploadType.AUDIO ->
            AttachmentMediaWithUploadData(AttachType.AUDIO.value, UploadInfo(token = reusableMediaParams.token))
        reusableMediaParams.uploadType == UploadType.FILE ->
            AttachmentMediaWithUploadData(AttachType.FILE.value, UploadInfo(token = reusableMediaParams.token))
        else -> throw IllegalStateException("Unknown upload type")
    })
    return SendMessage(sendMessage.text, attachments, sendMessage.notifyUser)
}

internal fun createSendMessageForImageToken(sendMessage: SendMessage, response: UploadInfo): SendMessage {
    return SendMessage(sendMessage.text, listOf(AttachmentPhoto(AttachType.IMAGE.value, response)), sendMessage.notifyUser)
}
