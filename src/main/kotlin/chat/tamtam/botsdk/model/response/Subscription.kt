package chat.tamtam.botsdk.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Subscription(
    val url: String,
    @SerialName("time") val creationTime: Long
)