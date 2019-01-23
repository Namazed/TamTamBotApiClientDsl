package chat.tamtam.botsdk.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SendMessage(
    val text: String,
    val attachments: List<AttachmentKeyboard> = emptyList(),
    @SerialName("notify") val notifyUser: Boolean = true
)
