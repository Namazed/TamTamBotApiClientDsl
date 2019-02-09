package chat.tamtam.botsdk.model.response

import kotlinx.serialization.Optional

class Chat(
    val chatId: Long,
    val type: ChatType,
    val status: Status,
    val title: String,
    val icon: ChatIcon,
    val lastEventTime: Long,
    val participantsCount: Int,
    @Optional val ownerId: Long = -1,
    @Optional val participants: List<Any> = emptyList()
)

enum class ChatType(val type: String) {
    DIALOG("dialog"),
    CHAT("chat"),
    CHANNEL("channel"),
    UNKNOWN("unknown")
}

enum class Status {
    ACTIVE,
    REMOVED,
    LEFT,
    CLOSED
}

class ChatIcon(
    val url: String
)