package chat.tamtam.botsdk.model.request

import kotlinx.serialization.Serializable

@Serializable
class Icon(
    val url: String,
    val photos: Map<String, String>
)