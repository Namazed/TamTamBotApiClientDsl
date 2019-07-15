package chat.tamtam.botsdk.model.request

import kotlinx.serialization.CompositeEncoder
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.internal.ArrayListSerializer
import kotlinx.serialization.internal.SerialClassDescImpl

@Serializable(with = BotSerializer::class)
class Bot(
    val name: String? = null,
    val username: String? = null,
    val description: String? = null,
    val commands: List<Command>? = null,
    val photo: Photo? = null
)

@Serializer(forClass = Photo::class)
internal object BotSerializer : KSerializer<Bot> {
    override val descriptor: SerialDescriptor = object : SerialClassDescImpl("Bot") {
        init {
            addElement("name", true)
            addElement("username", true)
            addElement("description", true)
            addElement("commands", true)
            addElement("photo", true)
        }
    }

    override fun serialize(encoder: Encoder, obj: Bot) {
        val compositeOutput: CompositeEncoder = encoder.beginStructure(descriptor)
        obj.name?.let {
            compositeOutput.encodeStringElement(descriptor, 0, it)
        }
        obj.username?.let {
            compositeOutput.encodeStringElement(descriptor, 1, it)
        }
        obj.description?.let {
            compositeOutput.encodeStringElement(descriptor, 2, it)
        }
        obj.commands?.let {
            compositeOutput.encodeSerializableElement(descriptor, 3, ArrayListSerializer(Command.serializer()), it)
        }
        obj.photo?.let {
            compositeOutput.encodeSerializableElement(descriptor, 4, PhotoSerializer, it)
        }
        compositeOutput.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): Bot {
        return Bot()
    }
}