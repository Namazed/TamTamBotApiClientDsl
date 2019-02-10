package chat.tamtam.botsdk.model.response

import kotlinx.serialization.Optional

class User(
    val userId: Long = -1,
    val name: String = "",
    @Optional val username: String = "",
    @Optional val avatarUrl: String = "",
    @Optional val fullAvatarUrl: String = ""
)