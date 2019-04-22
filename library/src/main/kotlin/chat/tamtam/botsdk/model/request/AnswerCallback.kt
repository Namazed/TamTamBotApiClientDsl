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

/**
 * This class need for answer user as replacement message (edit old message with keyboard) and/or answer user as notification(toast)
 * on button click (Callback by payload)
 *
 * @param message - message which replace old message (with keyboard) where user click on button, look at [SendMessage]
 */
@Serializable
class AnswerCallback(
    val message: SendMessage? = null,
    val userId: Long? = null,
    val notification: String? = null
) {
    @Serializer(forClass = AnswerCallback::class)
    companion object : KSerializer<AnswerCallback> {
        override val descriptor: SerialDescriptor = object : SerialClassDescImpl("AnswerCallback") {
            init {
                addElement("message", true)
                addElement("user_id", true)
                addElement("notification", true)
            }
        }

        override fun deserialize(decoder: Decoder): AnswerCallback {
            return AnswerCallback()
        }

        override fun serialize(encoder: Encoder, obj: AnswerCallback) {
            val compositeOutput: CompositeEncoder = encoder.beginStructure(descriptor)
            obj.message?.let {
                compositeOutput.encodeSerializableElement(descriptor, 0, SendMessage.serializer(), it)
            }
            obj.userId?.let {
                compositeOutput.encodeLongElement(descriptor, 1, it)
            }
            obj.notification?.let {
                compositeOutput.encodeStringElement(descriptor, 2, it)
            }
            compositeOutput.endStructure(descriptor)
        }
    }
}

internal fun createAnswerCallbackForKeyboard(sendMessage: SendMessage, keyboard: InlineKeyboard): AnswerCallback {
    return AnswerCallback(SendMessage(sendMessage.text, listOf(AttachmentKeyboard(AttachType.INLINE_KEYBOARD.value.toLowerCase(), keyboard)),
        sendMessage.notifyUser))
}

internal fun createAnswerCallbackForImageUrl(sendMessage: SendMessage, imageUrl: ImageUrl): AnswerCallback {
    return AnswerCallback(SendMessage(sendMessage.text,
        listOf(AttachmentPhotoWithUrl(AttachType.IMAGE.value, PayloadUrl(imageUrl.value))), sendMessage.notifyUser))
}

internal fun createAnswerCallbackForMediaToken(uploadType: UploadType, sendMessage: SendMessage, response: UploadInfo): AnswerCallback {
    return when (uploadType) {
        UploadType.VIDEO -> AnswerCallback(SendMessage(sendMessage.text, listOf(AttachmentMediaWithUploadData(AttachType.VIDEO.value, response)), sendMessage.notifyUser))
        UploadType.FILE -> AnswerCallback(SendMessage(sendMessage.text, listOf(AttachmentMediaWithUploadData(AttachType.FILE.value, response)), sendMessage.notifyUser))
        UploadType.AUDIO -> AnswerCallback(SendMessage(sendMessage.text, listOf(AttachmentMediaWithUploadData(AttachType.FILE.value, response)), sendMessage.notifyUser))
        else -> throw IllegalArgumentException("Incorrect uploadType for this method")
    }
}

internal fun createAnswerCallbackForReusablePhotoToken(sendMessage: SendMessage, reusablePhotoToken: String): AnswerCallback {
    return AnswerCallback(SendMessage(sendMessage.text, listOf(AttachmentPhotoWithToken(AttachType.IMAGE.value, PayloadToken(reusablePhotoToken))), sendMessage.notifyUser))
}

internal fun createAnswerCallbackForImageToken(sendMessage: SendMessage, response: UploadInfo): AnswerCallback {
    return AnswerCallback(SendMessage(sendMessage.text, listOf(AttachmentPhoto(AttachType.IMAGE.value, response)), sendMessage.notifyUser))
}

internal fun createAnswerCallbackForListImageTokens(sendMessage: SendMessage, uploadedTokens: List<UploadInfo>): AnswerCallback {
    return AnswerCallback(SendMessage(sendMessage.text, uploadedTokens.map { AttachmentPhoto(AttachType.IMAGE.value, it) }, sendMessage.notifyUser))
}