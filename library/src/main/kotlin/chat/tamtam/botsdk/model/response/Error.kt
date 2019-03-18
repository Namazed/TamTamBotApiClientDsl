package chat.tamtam.botsdk.model.response

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

@Serializable
class Error(
    @Optional val error: String = "",
    val code: String,
    val message: String
)