package chat.tamtam.botsdk.model

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Button(
    val type: String,
    @SerialName("text") val title: String,
    @Optional val intent: String = ButtonIntent.DEFAULT.value.toLowerCase(),
    val payload: String = ""
)


fun buttonTypeFrom(value: String) = when(value.toUpperCase()) {
    "CALLBACK" -> ButtonType.CALLBACK
    "LINK" -> ButtonType.LINK
    "REQUEST_CONTACT" -> ButtonType.REQUEST_CONTACT
    "REQUEST_GEO_LOCATION" -> ButtonType.REQUEST_GEO_LOCATION
    else -> throw IllegalArgumentException("Unknown value")
}

enum class ButtonType(val value: String) {
    CALLBACK("CALLBACK"),
    LINK("LINK"),
    REQUEST_CONTACT("REQUEST_CONTACT"),
    REQUEST_GEO_LOCATION("REQUEST_GEO_LOCATION")
}

enum class ButtonIntent(val value: String) {
    DEFAULT("DEFAULT"),
    POSITIVE("POSITIVE"),
    NEGATIVE("NEGATIVE")
}