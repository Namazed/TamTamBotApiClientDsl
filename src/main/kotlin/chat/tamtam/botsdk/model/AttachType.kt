package chat.tamtam.botsdk.model

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.withName

@Serializable
enum class AttachType(val value: String) {
    IMAGE("IMAGE"),
    VIDEO("VIDEO"),
    AUDIO("AUDIO"),
    FILE("FILE"),
    CONTACT("CONTACT"),
    STICKER("STICKER"),
    SHARE("SHARE"),
    INLINE_KEYBOARD("INLINE_KEYBOARD"),
    LOCATION("LOCATION")
}

fun attachTypeFrom(value: String) = when (value.toUpperCase()) {
    "IMAGE" -> AttachType.IMAGE
    "VIDEO" -> AttachType.VIDEO
    "AUDIO" -> AttachType.AUDIO
    "FILE" -> AttachType.FILE
    "CONTACT" -> AttachType.CONTACT
    "STICKER" -> AttachType.STICKER
    "SHARE" -> AttachType.SHARE
    "INLINE_KEYBOARD" -> AttachType.INLINE_KEYBOARD
    "LOCATION" -> AttachType.LOCATION
    else -> throw IllegalArgumentException("Unknown type")
}

internal object AttachTypeSerializer : KSerializer<AttachType> {
    override val descriptor: SerialDescriptor
        get() = StringDescriptor.withName("AttachType")

    override fun deserialize(input: Decoder): AttachType {
        return attachTypeFrom(input.decodeString())
    }

    override fun serialize(output: Encoder, obj: AttachType) {
        AttachType.serializer().serialize(output, obj)
    }
}