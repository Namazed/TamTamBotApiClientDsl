package chat.tamtam.botsdk.model.response

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Updates(
    @SerialName("updates") val listUpdates: List<Update>
)

@Serializable
class Update(
    val timestamp: Long,
    val updateType: UpdateType,
    @Optional val messageId: String = "",
    @Optional val chatId: Long = -1,
    @Optional val userId: Long = -1,
    @Optional val adminId: Long = -1,
    @Optional val inviterId: Long = -1,
    @Optional val message: Message = EMPTY_MESSAGE,
    @Optional val callback: Callback = EMPTY_CALLBACK,
    val marker: Long
)

enum class UpdateType(val type: String) {
    CALLBACK("message_callback"),
    MESSAGE_CREATED("message_created"),
    MESSAGE_REMOVED("message_removed"),
    MESSAGE_EDITED("message_edited"),
    MESSAGE_RESTORED("message_restored"),
    BOT_ADDED("bot_added"),
    BOT_REMOVED("bot_removed"),
    USER_ADDED("user_added"),
    USER_REMOVED("user_removed"),
    BOT_STARTED("bot_started"),
    CHAT_TITLE_CHANGED("chat_title_changed"),
    UNKNOWN("unknown")
}