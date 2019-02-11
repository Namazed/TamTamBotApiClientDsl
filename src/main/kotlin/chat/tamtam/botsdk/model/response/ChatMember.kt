package chat.tamtam.botsdk.model.response

import kotlinx.serialization.Optional

class ChatMembersResult(
    val members: List<ChatMember>,
    @Optional val marker: Long = -1L
)

class ChatMember(
    val userId: Long = -1,
    val name: String = "",
    @Optional val username: String = "",
    @Optional val avatarUrl: String = "",
    @Optional val fullAvatarUrl: String = "",
    val lastAccessTime: Long,
    val isOwner: Boolean,
    @Optional val isAdmin: Boolean? = null
)