package chat.tamtam.botsdk.model.prepared

import chat.tamtam.botsdk.model.response.Permissions

class ChatMembersList(
    val members: List<ChatMember>,
    val marker: Long?
)

data class ChatMember(
    val userInfo: User,
    val lastAccessTime: Long,
    val isOwner: Boolean,
    val isAdmin: Boolean?,
    val joinTime: Long?,
    val permissions: List<Permissions>?
)