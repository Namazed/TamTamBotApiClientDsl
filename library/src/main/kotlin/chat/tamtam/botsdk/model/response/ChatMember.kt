package chat.tamtam.botsdk.model.response

import chat.tamtam.botsdk.client.RequestsManager
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Optional
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.list
import kotlinx.serialization.withName

/**
 * You will get it after call [RequestsManager.getMembers]
 *
 * @param members - Participants in chat with time of last activity. Visible only for chat admins
 * @param marker - Pointer to the next data page, can be null
 */
@Serializable
class ChatMembersResult(
    @Serializable(ChatMembersSerializer::class) val members: List<ChatMember>,
    @Optional val marker: Long? = null
)

/**
 * Contains in [ChatMembersResult] or you get it if call [RequestsManager.getMembers]
 *
 * @param userId - unique identifier of participant in chat
 * @param name - users visible name
 * @param username - unique public user name. Can be null if user is not accessible or it is not set
 * @param avatarUrl - URL of avatar
 * @param fullAvatarUrl - URL of avatar of a bigger size
 * @param lastAccessTime - unix time when user last time do something in chat
 * @param isOwner - this participant is owner of this Chat
 * @param isAdmin - this participant is admin in this Chat
 * @param joinTime - time when member join to Chat
 * @param permissions - Items Enum:"read_all_messages" "add_remove_members" "add_admins" "change_chat_info" "pin_message" "write".
 *                      Permissions in chat if member is admin. null otherwise
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
    @SerialName("is_admin") @Optional val isAdmin: Boolean? = null,
    @SerialName("join_time") @Optional val joinTime: Long? = null,
    @Optional val permissions: List<String>? = null
)

enum class Permissions(val type: String) {
    READ_ALL_MESSAGES("read_all_messages"),
    ADD_OR_REMOVE_MEMBERS("add_remove_members"),
    ADD_ADMINS("add_admins"),
    CHANGE_CHAT_INFO("change_chat_info"),
    PIN_MESSAGE("pin_message"),
    WRITE("write"),
    UNKNOWN("unknown"),
}

fun permissionFrom(value: String) = when(value) {
    "read_all_messages" -> Permissions.READ_ALL_MESSAGES
    "add_remove_members" -> Permissions.ADD_OR_REMOVE_MEMBERS
    "add_admins" -> Permissions.ADD_ADMINS
    "change_chat_info" -> Permissions.CHANGE_CHAT_INFO
    "pin_message" -> Permissions.PIN_MESSAGE
    "write" -> Permissions.WRITE
    else -> Permissions.UNKNOWN
}

internal object PermissionsSerializer : KSerializer<Permissions> {
    override val descriptor: SerialDescriptor
        get() = StringDescriptor.withName("Permissions")

    override fun deserialize(decoder: Decoder): Permissions {
        return permissionFrom(decoder.decodeString())
    }

    @UseExperimental(ImplicitReflectionSerializer::class)
    override fun serialize(encoder: Encoder, obj: Permissions) {
        //todo write custom serializer
    }
}

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