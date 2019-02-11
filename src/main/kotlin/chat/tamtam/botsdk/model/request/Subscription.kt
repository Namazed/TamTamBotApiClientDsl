package chat.tamtam.botsdk.model.request

import kotlinx.serialization.Optional

class Subscription(
    val url: String,
    @Optional val filter: String = ""
)