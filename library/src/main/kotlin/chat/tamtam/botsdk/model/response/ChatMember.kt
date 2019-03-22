package chat.tamtam.botsdk.model.response

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Optional
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.list
import kotlinx.serialization.withName

/**
 * You will get it after call [chat.tamtam.botsdk.client.RequestsManager.getMembers]
 *
 * @param members - Participants in chat with time of last activity. Visible only for chat admins
 * @param marker - Pointer to the next data page
 */
@Serializable
class ChatMembersResult(
    @Serializable(ChatMembersSerializer::class) val members: List<ChatMember>,
    @Optional val marker: Long = -1L
)

/**
 * Contains in [ChatMembersResult]
 *
 * @param userId - unique identifier of participant in chat
 * @param name - users visible name
 * @param username - unique public user name. Can be null if user is not accessible or it is not set
 * @param avatarUrl - URL of avatar
 * @param fullAvatarUrl - URL of avatar of a bigger size
 * @param lastAccessTime - unix time when user last time do something in chat
 * @param isOwner - this participant is owner of this Chat
 * @param isAdmin - this participant is admin in this Chat
 */
@Serializable
data class ChatMember(
    @SerialName("user_id") val userId: Long = -1,
    val name: String = "",
    @Optional val username: String = "",
    @SerialName("avatar_url") @Optional val avatarUrl: String = "",
    @SerialName("full_avatar_url") @Optional val fullAvatarUrl: String = "",
    @SerialName("last_access_time") val lastAccessTime: Long,
    @SerialName("is_owner") val isOwner: Boolean,
    @SerialName("is_admin") @Optional val isAdmin: Boolean? = null
)

internal object ChatMembersSerializer : KSerializer<List<ChatMember>> {
    override val descriptor: SerialDescriptor
        get() = StringDescriptor.withName("ChatMembers")

    override fun deserialize(decoder: Decoder): List<ChatMember> {
        return ChatMember.serializer().list.deserialize(decoder)
    }

    override fun serialize(encoder: Encoder, obj: List<ChatMember>) {
        ChatMember.serializer().list.serialize(encoder, obj)
    }
}