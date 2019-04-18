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
        override fun deserialize(decoder: Decoder): Attachment {
            return AttachmentKeyboard("", EMPTY_INLINE_KEYBOARD)
        }

        override fun serialize(encoder: Encoder, obj: Attachment) {
            val type: AttachType = attachTypeFrom(obj.type)
            when (type) {
                AttachType.IMAGE -> {
                    when (obj) {
                        is AttachmentPhoto -> AttachmentPhoto.serializer().serialize(encoder, obj)
                        is AttachmentPhotoWithToken -> AttachmentPhotoWithToken.serializer().serialize(encoder, obj)
                        is AttachmentPhotoWithUrl -> AttachmentPhotoWithUrl.serializer().serialize(encoder, obj)
                    }
                }
                AttachType.VIDEO -> AttachmentMediaWithId.serializer().serialize(encoder, obj as AttachmentMediaWithId)
                AttachType.AUDIO -> AttachmentMediaWithId.serializer().serialize(encoder, obj as AttachmentMediaWithId)
                AttachType.FILE -> AttachmentMediaWithId.serializer().serialize(encoder, obj as AttachmentMediaWithId)
                AttachType.CONTACT -> AttachmentContact.serializer().serialize(encoder, obj as AttachmentContact)
                AttachType.STICKER -> AttachmentSticker.serializer().serialize(encoder, obj as AttachmentSticker)
                AttachType.SHARE -> TODO()
                AttachType.INLINE_KEYBOARD -> AttachmentKeyboard.serializer().serialize(encoder, obj as AttachmentKeyboard)
                AttachType.LOCATION -> AttachmentLocation.serializer().serialize(encoder, obj as AttachmentLocation)
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
class AttachmentPhotoWithToken(
    override val type: String,
    val payload: PayloadToken
) : Attachment

@Serializable
class AttachmentMediaWithId(
    override val type: String,
    val payload: UploadInfo
) : Attachment

@Serializable
class AttachmentPhotoWithUrl(
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
class PayloadToken(
    val token: String
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