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

@Serializable
class Chat(
    @SerialName("chat_id") val chatId: Long,
    @Serializable(ChatTypeSerializer::class) val type: ChatType,
    val status: Status,
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

enum class Status {
    ACTIVE,
    REMOVED,
    LEFT,
    CLOSED
}

@Serializable
class ChatIcon(
    val url: String
)

object ChatTypeSerializer : KSerializer<ChatType> {
    override val descriptor: SerialDescriptor
        get() = StringDescriptor.withName("ChatType")

    override fun deserialize(input: Decoder): ChatType {
        return chatTypeFrom(input.decodeString())
    }

    @UseExperimental(ImplicitReflectionSerializer::class)
    override fun serialize(output: Encoder, obj: ChatType) {
        ChatType::class.serializer().serialize(output, obj)
    }
}