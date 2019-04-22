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
import kotlinx.serialization.list
import kotlinx.serialization.serializer
import kotlinx.serialization.withName

@Serializable
internal class Message(
    @SerialName("body") val messageInfo: MessageInfo = MessageInfo(),
    val recipient: Recipient = Recipient(),
    val sender: User = User(),
    val timestamp: Long = -1,
    @Optional val link: Link? = null
)

@Serializable
internal class MessageInfo(
    @SerialName("mid") val messageId: String = "",
    @SerialName("seq") val sequenceIdInChat: Long = -1,
    @Serializable(ResponseAttachmentsSerializer::class) @Optional val attachments: List<Attachment> = emptyList(),
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