package chat.tamtam.botsdk.model.response

import kotlinx.serialization.Serializable

@Serializable
class SendMessage(
    val chatId: Long,
    val messageId: String
)