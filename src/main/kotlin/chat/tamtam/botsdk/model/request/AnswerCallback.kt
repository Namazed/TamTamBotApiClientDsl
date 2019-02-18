package chat.tamtam.botsdk.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class AnswerCallback(
    val message: SendMessage
)

@Serializable
class AnswerNotificationCallback(
    @SerialName("user_id") val userId: Long,
    val notification: String
)