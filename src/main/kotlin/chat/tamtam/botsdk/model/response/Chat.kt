package chat.tamtam.botsdk.model.response

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName

class Chat(
    val chatId: Long,
    val type: ChatType,
    val status: Status,
    val title: String,
    val icon: ChatIcon,
    val lastEventTime: Long,
    val participantsCount: Int,
    @Optional val ownerId: Long = -1,
    @Optional val participants: Map<Long, Long> = emptyMap(),
    @SerialName("is_public") val public: Boolean,
    @SerialName("link") @Optional val linkOnChat: String = "",
    val description: String
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