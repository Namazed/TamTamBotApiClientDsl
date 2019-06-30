package chat.tamtam.botsdk.model.response

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.list
import kotlinx.serialization.serializer
import kotlinx.serialization.withName

/**
 * This object you get when request messages or send message.
 * @param messageInfo - this is body of message look at [MessageInfo]
 * @param recipient - message recipient. Could be user or chat
 * @param sender - user that sent this message. Can be null if message has been posted on behalf of a channel.
 * @param timestamp - unix-time when message was created
 * @param link - forwarder or replied message
 * @param statistics - message statistics. Available only for channels when you call request get messages.
 */
@Serializable
internal class Message(
    @SerialName("body") val messageInfo: MessageInfo = MessageInfo(),
    val recipient: Recipient = Recipient(),
    val sender: User = User(),
    val timestamp: Long = -1,
    val link: Link? = null,
    @SerialName("stat") val statistics: Statistics = Statistics()
)

@Serializable
internal class MessageInfo(
    @SerialName("mid") val messageId: String = "",
    @SerialName("seq") val sequenceIdInChat: Long = -1,
    @Serializable(ResponseAttachmentsSerializer::class) val attachments: List<Attachment> = emptyList(),
    val text: String = ""
)

@Serializable
internal class Link(
    val type: String,
    val sender: User = User(),
    @SerialName("chat_id") val chatId: Long = -1,
    val message: MessageInfo
)

@Serializable
internal class Recipient(
    @SerialName("chat_id") val chatId: Long = -1,
    @Serializable(ChatTypeSerializer::class) @SerialName("chat_type") val chatType: ChatType = ChatType.UNKNOWN,
    @SerialName("user_id") val userId: Long = -1
)

@Serializable
internal class Statistics(
    val views: Int = -1
)

internal object ResponseAttachmentsSerializer : KSerializer<List<Attachment>> {
    override val descriptor: SerialDescriptor
        get() = StringDescriptor.withName("ResponseAttachments")

    @UseExperimental(ImplicitReflectionSerializer::class)
    override fun deserialize(decoder: Decoder): List<Attachment> {
        return Attachment::class.serializer().list.deserialize(decoder)
    }

    @UseExperimental(ImplicitReflectionSerializer::class)
    override fun serialize(encoder: Encoder, obj: List<Attachment>) {
        Attachment::class.serializer().list.serialize(encoder, obj)
    }
}

enum class LinkType(val value: String) {
    FORWARD("forward"),
    REPLY("reply")
}

fun linkTypeFrom(value: String) = when (value) {
    "forward" -> LinkType.FORWARD
    "reply" -> LinkType.REPLY
    else -> throw IllegalArgumentException("Unknown type")
}