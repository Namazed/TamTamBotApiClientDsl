package chat.tamtam.botsdk.model

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.serializer
import kotlinx.serialization.withName

@Serializable(AttachTypeSerializer::class)
enum class AttachType(val value: String) {
    IMAGE("image"),
    VIDEO("video"),
    AUDIO("audio"),
    FILE("file"),
    CONTACT("contact"),
    STICKER("sticker"),
    SHARE("share"),
    INLINE_KEYBOARD("inline_keyboard"),
    LOCATION("location")
}

fun attachTypeFrom(value: String) = when (value) {
    "image" -> AttachType.IMAGE
    "video" -> AttachType.VIDEO
    "audio" -> AttachType.AUDIO
    "file" -> AttachType.FILE
    "contact" -> AttachType.CONTACT
    "sticker" -> AttachType.STICKER
    "share" -> AttachType.SHARE
    "inline_keyboard" -> AttachType.INLINE_KEYBOARD
    "location" -> AttachType.LOCATION
    else -> throw IllegalArgumentException("Unknown type")
}

internal object AttachTypeSerializer : KSerializer<AttachType> {
    override val descriptor: SerialDescriptor
        get() = StringDescriptor.withName("AttachType")

    override fun deserialize(decoder: Decoder): AttachType {
        return attachTypeFrom(decoder.decodeString())
    }

    @UseExperimental(ImplicitReflectionSerializer::class)
    override fun serialize(encoder: Encoder, obj: AttachType) {
        AttachType::class.serializer().serialize(encoder, obj)
    }
}