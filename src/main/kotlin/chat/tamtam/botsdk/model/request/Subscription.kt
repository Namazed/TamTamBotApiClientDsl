package chat.tamtam.botsdk.model.request

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

@Serializable
class Subscription(
    val url: String,
    @Optional val filter: String = ""
)