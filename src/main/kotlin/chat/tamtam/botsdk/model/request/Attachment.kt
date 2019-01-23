package chat.tamtam.botsdk.model.request

import kotlinx.serialization.Serializable

@Serializable
class AttachmentKeyboard(
    val type: String = "",
    val payload: InlineKeyboard = EMPTY_INLINE_KEYBOARD
)

fun attachTypeFrom(value: String) = when(value.toUpperCase()) {
    "IMAGE" -> AttachType.IMAGE
    "VIDEO" -> AttachType.VIDEO
    "AUDIO" -> AttachType.AUDIO
    "FILE" -> AttachType.FILE
    "CONTACT" -> AttachType.CONTACT
    "STICKER" -> AttachType.STICKER
    "INLINE_KEYBOARD" -> AttachType.INLINE_KEYBOARD
    else -> throw IllegalArgumentException("Unknown type")
}

enum class AttachType(val value: String) {
    IMAGE("IMAGE"),
    VIDEO("VIDEO"),
    AUDIO("AUDIO"),
    FILE("FILE"),
    CONTACT("CONTACT"),
    STICKER("STICKER"),
    INLINE_KEYBOARD("INLINE_KEYBOARD")
}