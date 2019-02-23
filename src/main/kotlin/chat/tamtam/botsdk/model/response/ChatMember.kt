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

@Serializable
class ChatMembersResult(
    @Serializable(ChatMemebersSerializer::class) val members: List<ChatMember>,
    @Optional val marker: Long = -1L
)

@Serializable
class ChatMember(
    @SerialName("user_id") val userId: Long = -1,
    val name: String = "",
    @Optional val username: String = "",
    @SerialName("avatar_url") @Optional val avatarUrl: String = "",
    @SerialName("full_avatar_url") @Optional val fullAvatarUrl: String = "",
    @SerialName("last_access_time") val lastAccessTime: Long,
    @SerialName("is_owner") val isOwner: Boolean,
    @SerialName("is_admin") @Optional val isAdmin: Boolean? = null
)

object ChatMemebersSerializer : KSerializer<List<ChatMember>> {
    override val descriptor: SerialDescriptor
        get() = StringDescriptor.withName("ChatMembers")

    override fun deserialize(input: Decoder): List<ChatMember> {
        return ChatMember.serializer().list.deserialize(input)
    }

    override fun serialize(output: Encoder, obj: List<ChatMember>) {
        ChatMember.serializer().list.serialize(output, obj)
    }
}