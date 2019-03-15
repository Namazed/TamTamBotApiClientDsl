package chat.tamtam.botsdk.model.response

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Optional
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.serializer
import kotlinx.serialization.withName

/**
 * You will get it after call [chat.tamtam.botsdk.client.RequestsManager.getAllChats] or [chat.tamtam.botsdk.client.RequestsManager.getChat]
 *
 * @param chatId - chats identifier
 * @param type - type of chat. One of: dialog, chat, channel [ChatType]
 * @param status - chat status. One of: [Status]
 * @param title - visible title of chat
 * @param icon - icon of chat
 * @param lastEventTime - time of last event occured in chat
 * @param participantsCount - number of people in chat. Always 2 for dialog chat type
 * @param ownerId - identifier of chat owner. Visible only for chat admins
 * @param participants - participants in chat with time of last activity. Visible only for chat admins
 * @param public - is current chat publicly available. Always false for dialogs
 * @param linkOnChat - link on chat if it is public
 * @param description - chat description
 */
@Serializable
class Chat(
    @SerialName("chat_id") val chatId: Long,
    @Serializable(ChatTypeSerializer::class) val type: ChatType,
    @Serializable(ChatStatusSerializer::class) val status: Status,
    val title: String,
    val icon: ChatIcon,
    @SerialName("last_event_time") val lastEventTime: Long,
    @SerialName("participants_count") val participantsCount: Int,
    @SerialName("owner_id") @Optional val ownerId: Long = -1,
    @Optional val participants: Map<Long, Long> = emptyMap(),
    @SerialName("is_public") val public: Boolean,
    @SerialName("link") @Optional val linkOnChat: String = "",
    val description: String
)

fun chatTypeFrom(value: String) = when(value) {
    "dialog" -> ChatType.DIALOG
    "chat" -> ChatType.CHAT
    "channel" -> ChatType.CHANNEL
    else -> ChatType.UNKNOWN
}

enum class ChatType(val type: String) {
    DIALOG("dialog"),
    CHAT("chat"),
    CHANNEL("channel"),
    UNKNOWN("unknown")
}

fun chatStatusFrom(value: String) = when(value) {
    "active" -> Status.ACTIVE
    "removed" -> Status.REMOVED
    "left" -> Status.LEFT
    "closed" -> Status.CLOSED
    "suspended" -> Status.SUSPENDED
    else -> Status.UNKNOWN
}

enum class Status(val value: String) {
    /**
     * bot is active member of chat
     */
    ACTIVE("active"),
    /**
     * bot was kicked
     */
    REMOVED("removed"),
    /**
     * bot intentionally left chat
     */
    LEFT("left"),
    /**
     * chat was closed
     */
    CLOSED("closed"),
    /**
     * bot suspended in chat
     */
    SUSPENDED("suspended"),
    UNKNOWN("unknown")
}

/**
 * Icon of chat contains in [Chat]
 *
 * @param url - URL of image
 */
@Serializable
class ChatIcon(
    val url: String
)

internal object ChatTypeSerializer : KSerializer<ChatType> {
    override val descriptor: SerialDescriptor
        get() = StringDescriptor.withName("ChatType")

    override fun deserialize(decoder: Decoder): ChatType {
        return chatTypeFrom(decoder.decodeString())
    }

    @UseExperimental(ImplicitReflectionSerializer::class)
    override fun serialize(encoder: Encoder, obj: ChatType) {
        ChatType::class.serializer().serialize(encoder, obj)
    }
}

internal object ChatStatusSerializer : KSerializer<Status> {
    override val descriptor: SerialDescriptor
        get() = StringDescriptor.withName("ChatStatus")

    override fun deserialize(decoder: Decoder): Status {
        return chatStatusFrom(decoder.decodeString())
    }

    @UseExperimental(ImplicitReflectionSerializer::class)
    override fun serialize(encoder: Encoder, obj: Status) {
        Status::class.serializer().serialize(encoder, obj)
    }
}