package chat.tamtam.botsdk.model.request

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class AnswerCallback(
    @SerialName("user_id") val userId: Long,
    val message: SendMessage,
    @Optional val notification: String? = null
)