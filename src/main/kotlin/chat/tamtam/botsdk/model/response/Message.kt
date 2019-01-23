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
    val mid: String = "",
    val seq: Long = -1,
    val text: String = ""
)

@Serializable
class Recipient(
    @Optional val chatId: Long = -1,
    @Optional val userId: Long = -1
)

fun isNotEmptyMessage(message: Message?) = message != null && message.timestamp != -1L