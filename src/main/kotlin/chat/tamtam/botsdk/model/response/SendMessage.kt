package chat.tamtam.botsdk.model.response

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This message you will get as Response after sent message to chat
 *
 * @param chatId - identifier of chat where created message
 * @param messageId - unique identifier of created message
 * @param messageId - in most cases same as [chatId]
 */
@Serializable
class SendMessage(
    @SerialName("chat_id") val chatId: Long,
    @SerialName("message_id") val messageId: String,
    @Optional @SerialName("recipient_id") val recipientId: Long = -1
)