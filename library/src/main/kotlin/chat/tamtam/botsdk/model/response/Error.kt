package chat.tamtam.botsdk.model.response

import kotlinx.serialization.Serializable

@Serializable
class Error(
    val error: String = "",
    val code: String,
    val message: String
)