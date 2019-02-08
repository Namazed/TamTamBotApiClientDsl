package chat.tamtam.botsdk.model.response

import chat.tamtam.botsdk.model.Button
import kotlinx.serialization.Optional

class Attachment(
    val type: String,
    @Optional val callbackId: String,
    val payload: Payload
)

class Payload(
    @Optional val photoId: Long = -1,
    @Optional val url: String = "",
    @Optional val vcfInfo: String = "",
    @Optional val tamInfo: User,
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

enum class AttachType(val value: String) {
    IMAGE("IMAGE"),
    VIDEO("VIDEO"),
    AUDIO("AUDIO"),
    FILE("FILE"),
    CONTACT("CONTACT"),
    STICKER("STICKER"),
    INLINE_KEYBOARD("INLINE_KEYBOARD")
}