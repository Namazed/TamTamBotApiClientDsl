package chat.tamtam.botsdk.model.response

import kotlinx.serialization.Serializable

@Serializable
internal class Messages(
    val messages: List<Message>
)