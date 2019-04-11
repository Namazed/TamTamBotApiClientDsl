package chat.tamtam.botsdk.model.response

import kotlinx.serialization.Serializable

/**
 * This message you will get as Response after sent message to chat
 *
 * @param message - Message in chat
 */
@Serializable
internal class SendMessage(
    val message: Message
)