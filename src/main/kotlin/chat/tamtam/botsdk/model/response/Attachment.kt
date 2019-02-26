package chat.tamtam.botsdk.model.response

import chat.tamtam.botsdk.model.Button
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Optional
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.withName

@Serializable
class Attachment(
    @Serializable(AttachTypeSerializer::class) val type: AttachType,
    @SerialName("callback_id") @Optional val callbackId: String = "",
    val payload: Payload
)

@Serializable
class Payload(
    @SerialName("photo_id") @Optional val photoId: Long = -1,
    @Optional val url: String = "",
    @SerialName("vcf_info") @Optional val vcfInfo: String = "",
    @SerialName("tam_info") @Optional val tamInfo: User = User(),
    @Optional val buttons: List<List<Button>> = emptyList()
)

fun attachTypeFrom(value: String) = when (value.toUpperCase()) {
    "IMAGE" -> AttachType.IMAGE
    "VIDEO" -> AttachType.VIDEO
    "AUDIO" -> AttachType.AUDIO
    "FILE" -> AttachType.FILE
    "CONTACT" -> AttachType.CONTACT
    "STICKER" -> AttachType.STICKER
    "INLINE_KEYBOARD" -> AttachType.INLINE_KEYBOARD
    else -> throw IllegalArgumentException("Unknown type")
}

@Serializable
enum class AttachType(val value: String) {
    IMAGE("IMAGE"),
    VIDEO("VIDEO"),
    AUDIO("AUDIO"),
    FILE("FILE"),
    CONTACT("CONTACT"),
    STICKER("STICKER"),
    INLINE_KEYBOARD("INLINE_KEYBOARD")
}

object AttachTypeSerializer : KSerializer<AttachType> {
    override val descriptor: SerialDescriptor
        get() = StringDescriptor.withName("AttachType")

    override fun deserialize(input: Decoder): AttachType {
        return attachTypeFrom(input.decodeString())
    }

    override fun serialize(output: Encoder, obj: AttachType) {
        AttachType.serializer().serialize(output, obj)
    }
}