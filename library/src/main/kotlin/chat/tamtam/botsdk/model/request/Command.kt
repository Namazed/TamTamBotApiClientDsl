package chat.tamtam.botsdk.model.request

import kotlinx.serialization.Serializable

@Serializable
class Command(
    val name: String,
    val description: String? = null
)