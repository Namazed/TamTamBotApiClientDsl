package chat.tamtam.botsdk.model

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Optional
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.withName

@Serializable
class Button(
    val type: String,
    @SerialName("text") val title: String,
    @Optional val intent: String = ButtonIntent.DEFAULT.value,
    val payload: String = ""
)


fun buttonTypeFrom(value: String) = when(value) {
    "callback" -> ButtonType.CALLBACK
    "link" -> ButtonType.LINK
    "request_contact" -> ButtonType.REQUEST_CONTACT
    "request_geo_location" -> ButtonType.REQUEST_GEO_LOCATION
    else -> throw IllegalArgumentException("Unknown value")
}

fun buttonIntentFrom(value: String) = when(value) {
    "default" -> ButtonIntent.DEFAULT
    "positive" -> ButtonIntent.POSITIVE
    "negative" -> ButtonIntent.NEGATIVE
    else -> throw IllegalArgumentException("Unknown value")
}

@Serializable(ButtonTypeSerializer::class)
enum class ButtonType(val value: String) {
    CALLBACK("callback"),
    LINK("link"),
    REQUEST_CONTACT("request_contact"),
    REQUEST_GEO_LOCATION("request_geo_location")
}

@Serializable(ButtonIntentSerializer::class)
enum class ButtonIntent(val value: String) {
    DEFAULT("default"),
    POSITIVE("positive"),
    NEGATIVE("negative")
}

internal object ButtonTypeSerializer : KSerializer<ButtonType> {
    override val descriptor: SerialDescriptor
        get() = StringDescriptor.withName("AttachType")

    override fun deserialize(decoder: Decoder): ButtonType {
        return buttonTypeFrom(decoder.decodeString())
    }

    @UseExperimental(ImplicitReflectionSerializer::class)
    override fun serialize(encoder: Encoder, obj: ButtonType) {
        //todo write custom serialize
    }
}

internal object ButtonIntentSerializer : KSerializer<ButtonIntent> {
    override val descriptor: SerialDescriptor
        get() = StringDescriptor.withName("AttachType")

    override fun deserialize(decoder: Decoder): ButtonIntent {
        return buttonIntentFrom(decoder.decodeString())
    }

    @UseExperimental(ImplicitReflectionSerializer::class)
    override fun serialize(encoder: Encoder, obj: ButtonIntent) {
        //todo write custom serialize
    }
}