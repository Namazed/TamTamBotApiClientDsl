package chat.tamtam.botsdk.model.response

import kotlinx.serialization.Serializable

@Serializable
class Messages(
    val messages: List<Message>
)