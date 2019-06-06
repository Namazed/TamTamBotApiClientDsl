package chat.tamtam.botsdk.model.request

import kotlinx.serialization.Serializable

/**
 * Use this class if you want change chat info: title or icon
 *
 * @param title - Optional. New title of chat
 * @param icon - Optional. New icon of chat
 */
@Serializable
class ChatInfo(
    val title: String? = null,
    val icon: Icon? = null
)