package chat.tamtam.botsdk.model.request

import chat.tamtam.botsdk.model.AttachType
import chat.tamtam.botsdk.model.ImageUrl
import chat.tamtam.botsdk.model.response.UploadInfo
import kotlinx.serialization.CompositeEncoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.internal.SerialClassDescImpl
import kotlinx.serialization.list

@Serializable
@Polymorphic
interface SendMessageContract {
    val notifyUser: Boolean

    @Serializer(forClass = SendMessageContract::class)
    companion object : KSerializer<SendMessageContract> {

        override val descriptor: SerialDescriptor = object : SerialClassDescImpl("SendMessageContract") {
            init {
                addElement("text", true)
                addElement("attachments", true)
                addElement("notify", true)
                addElement("link", true)
            }
        }

        override fun serialize(encoder: Encoder, obj: SendMessageContract) {
            val sendMessage = obj as SendMessage
            val compositeOutput: CompositeEncoder = encoder.beginStructure(descriptor)
            sendMessage.text?.let {
                compositeOutput.encodeStringElement(descriptor, 0, it)
            }
            sendMessage.attachments?.let {
                compositeOutput.encodeSerializableElement(descriptor, 1, Attachment.serializer().list, it)
            }
            compositeOutput.encodeBooleanElement(descriptor, 2, obj.notifyUser)
            sendMessage.link?.let {
                compositeOutput.encodeSerializableElement(descriptor, 3, LinkOnMessageSerializer, it)
            }
            compositeOutput.endStructure(descriptor)
        }
    }
}

/**
 * This class need use when you want send message.
 *
 * @param text - text content of message
 * @param attachments - list of attaches, for example [AttachmentKeyboard], [AttachmentLocation], [AttachmentMediaWithId] and etc.
 * @param notifyUser - this flag use in chat, when you want send quiet message or not.
 */
@Serializable
class SendMessage(
    val text: String? = null,
    val attachments: List<Attachment>? = null,
    @SerialName("notify") override val notifyUser: Boolean = true,
    val link: LinkOnMessage? = null
): SendMessageContract

internal fun createSendMessageForKeyboard(sendMessage: SendMessage, keyboard: InlineKeyboard): SendMessage {
    return SendMessage(sendMessage.text, listOf(AttachmentKeyboard(AttachType.INLINE_KEYBOARD.value, keyboard)), sendMessage.notifyUser)
}

internal fun createSendMessageForImageUrl(sendMessage: SendMessage, imageUrl: ImageUrl): SendMessage {
    return SendMessage(sendMessage.text,
        listOf(AttachmentPhotoWithUrl(AttachType.IMAGE.value, PayloadUrl(imageUrl.value))), sendMessage.notifyUser)
}

internal fun createSendMessageForMediaToken(uploadType: UploadType, sendMessage: SendMessage, response: UploadInfo): SendMessage {
    return when (uploadType) {
        UploadType.VIDEO -> SendMessage(sendMessage.text, listOf(AttachmentMediaWithId(AttachType.VIDEO.value, response)), sendMessage.notifyUser)
        UploadType.FILE -> SendMessage(sendMessage.text, listOf(AttachmentMediaWithId(AttachType.FILE.value, response)), sendMessage.notifyUser)
        UploadType.AUDIO -> SendMessage(sendMessage.text, listOf(AttachmentMediaWithId(AttachType.FILE.value, response)), sendMessage.notifyUser)
        else -> throw IllegalArgumentException("Incorrect uploadType for this method")
    }
}

internal fun createSendMessageForReusablePhotoToken(sendMessage: SendMessage, reusablePhotoToken: String): SendMessage {
    return SendMessage(sendMessage.text, listOf(AttachmentPhotoWithToken(AttachType.IMAGE.value, PayloadToken(reusablePhotoToken))), sendMessage.notifyUser)
}

internal fun createSendMessageForImageToken(sendMessage: SendMessage, response: UploadInfo): SendMessage {
    return SendMessage(sendMessage.text, listOf(AttachmentPhoto(AttachType.IMAGE.value, response)), sendMessage.notifyUser)
}

internal fun createSendMessageForListImageTokens(sendMessage: SendMessage, uploadedTokens: List<UploadInfo>): SendMessage {
    return SendMessage(sendMessage.text, uploadedTokens.map { AttachmentPhoto(AttachType.IMAGE.value, it) }, sendMessage.notifyUser)
}
