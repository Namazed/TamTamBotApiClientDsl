package chat.tamtam.botsdk.model.request

import kotlinx.serialization.Optional

class ChatInfo(
    @Optional val title: String? = null,
    @Optional val icon: Icon? = null
)