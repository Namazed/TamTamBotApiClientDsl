package chat.tamtam.botsdk.model.request

import kotlinx.serialization.Serializable

@Serializable
class AttachmentKeyboard(
    val type: String = "",
    val payload: InlineKeyboard = EMPTY_INLINE_KEYBOARD
)