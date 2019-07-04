package chat.tamtam.botsdk.model.response

import kotlinx.serialization.CompositeDecoder
import kotlinx.serialization.CompositeEncoder
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializer
import kotlinx.serialization.internal.HashMapSerializer
import kotlinx.serialization.internal.SerialClassDescImpl
import kotlinx.serialization.serializer

/**
 * You will get it after you uploaded some type of [chat.tamtam.botsdk.model.request.AttachmentContract]
 * Or you can create object of this class if you want send id for reuse some media in other message, but only for id and fileId
 *
 * @param photos - you will get it if you uploaded [chat.tamtam.botsdk.model.request.UploadType.PHOTO]
 * @param id - you will get it unique identifier if you uploaded [chat.tamtam.botsdk.model.request.UploadType.VIDEO]
 * or [chat.tamtam.botsdk.model.request.UploadType.AUDIO]
 * @param fileId - you will get it unique identifier of file if you uploaded [chat.tamtam.botsdk.model.request.UploadType.FILE]
 * @param token - reusable token which you need send with message instead of fileId or id. Actual for file, audio, video
 */
@Serializable(with = UploadInfoSerializer::class)
class UploadInfo(
    val photos: Map<String, PhotoToken>? = null,
    val id: Long? = null,
    val fileId: Long? = null,
    val token: String? = null
)

@Serializer(forClass = UploadInfo::class)
internal object UploadInfoSerializer : KSerializer<UploadInfo> {
    override val descriptor: SerialDescriptor = object : SerialClassDescImpl("UploadInfo") {
        init {
            addElement("photos", true)
            addElement("id", true)
            addElement("fileId", true)
            addElement("token", true)
        }
    }

    override fun serialize(encoder: Encoder, obj: UploadInfo) {
        val compositeOutput: CompositeEncoder = encoder.beginStructure(descriptor)
        obj.photos?.let {
            compositeOutput.encodeSerializableElement(descriptor, 0, HashMapSerializer(String.serializer(), PhotoToken.serializer()), it)
        }
        obj.token?.let {
            compositeOutput.encodeStringElement(descriptor, 3, it)
        } ?: obj.id?.let {
            compositeOutput.encodeLongElement(descriptor, 1, it)
        } ?: obj.fileId?.let {
            compositeOutput.encodeLongElement(descriptor, 2, it)
        }
        compositeOutput.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): UploadInfo {
        val compositeDecoder: CompositeDecoder = decoder.beginStructure(descriptor)
        var photos: Map<String, PhotoToken>? = null
        var id: Long? = null
        var fileId: Long? = null
        var token: String? = null
        loop@ while (true) {
            when (val i = compositeDecoder.decodeElementIndex(descriptor)) {
                0 -> photos = compositeDecoder.decodeSerializableElement(descriptor, i, HashMapSerializer(String.serializer(), PhotoToken.serializer()))
                1 -> id = compositeDecoder.decodeLongElement(descriptor, i)
                2 -> fileId = compositeDecoder.decodeLongElement(descriptor, i)
                3 -> token = compositeDecoder.decodeStringElement(descriptor, i)
                CompositeDecoder.READ_DONE -> break@loop
                else -> throw SerializationException("Unknown index $i")
            }
        }
        compositeDecoder.endStructure(descriptor)
        return UploadInfo(photos, id, fileId, token)
    }
}