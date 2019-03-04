package chat.tamtam.botsdk.model.request

import chat.tamtam.botsdk.model.AttachType
import chat.tamtam.botsdk.model.attachTypeFrom
import chat.tamtam.botsdk.model.response.UploadInfo
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.withName

@Serializable
interface Attachment {
    val type: String

    @Serializer(forClass = Attachment::class)
    companion object : KSerializer<Attachment> {
        override val descriptor: SerialDescriptor
            get() = StringDescriptor.withName("Attachment")

        /**
         * This is default deserialize, because it not needed.
         */
        override fun deserialize(input: Decoder): Attachment {
            val type: AttachType = attachTypeFrom(input.decodeString())
            return AttachmentKeyboard("", EMPTY_INLINE_KEYBOARD)
        }

        override fun serialize(output: Encoder, obj: Attachment) {
            val type: AttachType = attachTypeFrom(obj.type)
            when (type) {
                AttachType.IMAGE -> {
                    AttachmentPhoto.serializer().serialize(output, obj as AttachmentPhoto)
                }
                AttachType.VIDEO -> TODO()
                AttachType.AUDIO -> TODO()
                AttachType.FILE -> TODO()
                AttachType.CONTACT -> TODO()
                AttachType.STICKER -> TODO()
                AttachType.SHARE -> TODO()
                AttachType.INLINE_KEYBOARD -> {
                    AttachmentKeyboard.serializer().serialize(output, obj as AttachmentKeyboard)
                }
                AttachType.LOCATION -> TODO()
            }
        }
    }
}

@Serializable
class AttachmentKeyboard(
    override val type: String,
    val payload: InlineKeyboard
) : Attachment

@Serializable
class AttachmentContact(
    override val type: String,
    val payload: PayloadContact
) : Attachment

@Serializable
class AttachmentSticker(
    override val type: String,
    val payload: PayloadSticker
) : Attachment

@Serializable
class AttachmentPhoto(
    override val type: String,
    val payload: UploadInfo
) : Attachment

@Serializable
class AttachmentMediaWithId(
    override val type: String,
    val payload: UploadInfo
) : Attachment

@Serializable
class AttachmentMediaWithUrl(
    override val type: String,
    val payload: PayloadUrl
) : Attachment

@Serializable
class AttachmentLocation(
    override val type: String,
    val latitude: Double,
    val longitude: Double
) : Attachment

@Serializable
class PayloadUrl(
    val url: String
)

@Serializable
class PayloadSticker(
    val code: String
)

@Serializable
class PayloadContact(
    val name: String,
    val contactId: Long,
    val vcfInfo: String,
    val vcfPhone: String
)