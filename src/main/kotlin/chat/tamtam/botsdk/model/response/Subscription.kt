package chat.tamtam.botsdk.model.response

import kotlinx.serialization.SerialName

class Subscription(
    val url: String,
    @SerialName("time") val creationTime: Long
)