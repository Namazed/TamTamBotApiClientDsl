package chat.tamtam.botsdk.model.response

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

val EMPTY_MESSAGE = Message()

@Serializable
class Message(
    @SerialName("message") val messageInfo: MessageInfo = MessageInfo(),
    val recipient: Recipient = Recipient(),
    val sender: User = User(),
    val timestamp: Long = -1
)

@Serializable
class MessageInfo(
    @SerialName("mid") val messageId: String = "",
    @SerialName("seq") val sequenceIdInChat: Long = -1,
    @Optional val attachments: List<Attachment> = emptyList(),
    val text: String = ""
)

@Serializable
class Link(
    val type: String,
    val message: Message
)

@Serializable
class Recipient(
    @Optional val chatId: Long = -1
)

fun isNotEmptyMessage(message: Message?) = message != null && message.timestamp != -1L

enum class LinkType(val type: String) {
    FORWARD("FORWARD"),
    REPLY("REPLY")
}

fun linkTypeFrom(value: String) = when (value.toUpperCase()) {
    "FORWARD" -> AttachType.IMAGE
    "REPLY" -> AttachType.VIDEO
    else -> throw IllegalArgumentException("Unknown type")
}