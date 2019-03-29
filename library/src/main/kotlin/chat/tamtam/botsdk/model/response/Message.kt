package chat.tamtam.botsdk.model.response

import chat.tamtam.botsdk.model.AttachType
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

val EMPTY_MESSAGE = Message()

@Serializable
data class Message(
    @SerialName("body") val messageInfo: MessageInfo = MessageInfo(),
    val recipient: Recipient = Recipient(),
    val sender: User = User(),
    val timestamp: Long = -1,
    @Optional val link: Link? = null
)

@Serializable
data class MessageInfo(
    @SerialName("mid") val messageId: String = "",
    @SerialName("seq") val sequenceIdInChat: Long = -1,
    @Serializable(ResponseAttachmentsSerializer::class) @Optional val attachments: List<Attachment> = emptyList(),
    val text: String = ""
)

@Serializable
data class Link(
    val type: String,
    val message: Message
)

@Serializable
data class Recipient(
    @SerialName("chat_id") @Optional val chatId: Long = -1,
    @Serializable(ChatTypeSerializer::class) @SerialName("chat_type") val chatType: ChatType = ChatType.UNKNOWN,
    @Optional @SerialName("user_id") val userId: Long = -1
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

internal fun isNotEmptyMessage(message: Message?) = message != null && message.timestamp != -1L

enum class LinkType(val type: String) {
    FORWARD("FORWARD"),
    REPLY("REPLY")
}

fun linkTypeFrom(value: String) = when (value.toUpperCase()) {
    "FORWARD" -> AttachType.IMAGE
    "REPLY" -> AttachType.VIDEO
    else -> throw IllegalArgumentException("Unknown type")
}