package chat.tamtam.botsdk.model.request

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class AnswerCallback(
    @SerialName("user_id") @Optional val userId: Long = -1,
    val message: SendMessage? = null,
    @Optional val notification: String? = null
)