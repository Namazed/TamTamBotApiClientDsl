package chat.tamtam.botsdk.model.response

import kotlinx.serialization.Optional

class Error(
    @Optional val error: String = "",
    val code: String,
    val message: String
)