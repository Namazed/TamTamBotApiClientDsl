package chat.tamtam.botsdk.model.request

import chat.tamtam.botsdk.model.response.PhotoToken
import kotlinx.serialization.CompositeEncoder
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.internal.HashMapSerializer
import kotlinx.serialization.internal.SerialClassDescImpl
import kotlinx.serialization.serializer

@Serializable(with = PhotoSerializer::class)
class Photo(
    val url: String? = null,
    val token: String? = null,
    val photos: Map<String, PhotoToken>? = null
)

@Serializer(forClass = Photo::class)
internal object PhotoSerializer : KSerializer<Photo> {
    override val descriptor: SerialDescriptor = object : SerialClassDescImpl("Photo") {
        init {
            addElement("url", true)
            addElement("token", true)
            addElement("photos", true)
        }
    }

    override fun serialize(encoder: Encoder, obj: Photo) {
        val compositeOutput: CompositeEncoder = encoder.beginStructure(descriptor)
        obj.url?.let {
            compositeOutput.encodeStringElement(descriptor, 0, it)
        } ?: obj.token?.let {
            compositeOutput.encodeStringElement(descriptor, 1, it)
        } ?: obj.photos?.let {
            compositeOutput.encodeSerializableElement(descriptor, 2, HashMapSerializer(String.serializer(), PhotoToken.serializer()), it)
        }
        compositeOutput.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): Photo {
        return Photo()
    }
}