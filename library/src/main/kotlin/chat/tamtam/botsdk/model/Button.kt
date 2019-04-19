package chat.tamtam.botsdk.model

import kotlinx.serialization.CompositeDecoder
import kotlinx.serialization.CompositeEncoder
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.internal.SerialClassDescImpl

/**
 * @param type - type of Button [ButtonType]
 * @param title - visible text of button
 * @param intent - intent of button. Affects clients representation. [ButtonIntent]
 * @param url - this url you need set if send button with type [ButtonType.LINK]
 * @param payload - button payload
 */
@Serializable(with = ButtonSerializer::class)
class Button(
    val type: ButtonType,
    val title: String,
    val intent: ButtonIntent = ButtonIntent.DEFAULT,
    val url: String = "",
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

enum class ButtonType(val value: String) {
    CALLBACK("callback"),
    LINK("link"),
    REQUEST_CONTACT("request_contact"),
    REQUEST_GEO_LOCATION("request_geo_location")
}

enum class ButtonIntent(val value: String) {
    DEFAULT("default"),
    POSITIVE("positive"),
    NEGATIVE("negative")
}

internal object ButtonSerializer : KSerializer<Button> {
    override val descriptor: SerialDescriptor = object : SerialClassDescImpl("Button") {
        init {
            addElement("type")
            addElement("text")
            addElement("intent", true)
            addElement("url", true)
            addElement("payload", true)
        }
    }

    override fun serialize(encoder: Encoder, obj: Button) {
        val compositeOutput: CompositeEncoder = encoder.beginStructure(descriptor)
        compositeOutput.encodeStringElement(descriptor, 0, obj.type.value)
        compositeOutput.encodeStringElement(descriptor, 1, obj.title)
        if (obj.type == ButtonType.CALLBACK) {
            compositeOutput.encodeStringElement(descriptor, 2, obj.intent.value)
        }
        if (obj.url.isNotEmpty()) {
            compositeOutput.encodeStringElement(descriptor, 3, obj.url)
        }
        if (obj.payload.isNotEmpty()) {
            compositeOutput.encodeStringElement(descriptor, 4, obj.payload)
        }
        compositeOutput.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): Button {
        val compositeDecoder: CompositeDecoder = decoder.beginStructure(descriptor)
        var type = ""
        var title = ""
        var intent: String? = null
        var url = ""
        var payload = ""
        loop@ while (true) {
            when (val i = compositeDecoder.decodeElementIndex(descriptor)) {
                0 -> type = compositeDecoder.decodeStringElement(descriptor, i)
                1 -> title = compositeDecoder.decodeStringElement(descriptor, i)
                2 -> intent = compositeDecoder.decodeStringElement(descriptor, i)
                3 -> url = compositeDecoder.decodeStringElement(descriptor, i)
                4 -> payload = compositeDecoder.decodeStringElement(descriptor, i)
                CompositeDecoder.READ_DONE -> break@loop
                else -> throw SerializationException("Unknown index $i")
            }
        }
        compositeDecoder.endStructure(descriptor)
        return Button(buttonTypeFrom(type), title, buttonIntentFrom(intent ?: "default"), url, payload)
    }
}
