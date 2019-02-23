package chat.tamtam.botsdk.model.request

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

@Serializable
class ChatInfo(
    @Optional val title: String? = null,
    @Optional val icon: Icon? = null
)