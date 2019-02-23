package chat.tamtam.botsdk.model.response

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SendMessage(
    @SerialName("chat_id") val chatId: Long,
    @SerialName("message_id") val messageId: String,
    @Optional @SerialName("recipient_id") val recipientId: Long = -1
)