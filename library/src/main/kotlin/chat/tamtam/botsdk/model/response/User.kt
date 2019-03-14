package chat.tamtam.botsdk.model.response

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class User(
    @SerialName("user_id") val userId: Long = -1,
    val name: String = "",
    @Optional val username: String = "",
    @SerialName("avatar_url") @Optional val avatarUrl: String = "",
    @SerialName("full_avatar_url") @Optional val fullAvatarUrl: String = ""
)