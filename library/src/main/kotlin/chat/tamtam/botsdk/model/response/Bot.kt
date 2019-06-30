package chat.tamtam.botsdk.model.response

import chat.tamtam.botsdk.model.request.Command
import kotlinx.serialization.SerialName

class Bot(
    @SerialName("user_id") val userId: Long = -1,
    val name: String = "",
    val username: String = "",
    @SerialName("avatar_url") val avatarUrl: String = "",
    @SerialName("full_avatar_url") val fullAvatarUrl: String = "",
    val commands: List<Command> = emptyList(),
    val description: String = ""
)