package chat.tamtam.botsdk.model.request

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
abstract class AttachmentContract {

    @Serializer(forClass = AttachmentContract::class)
    companion object : KSerializer<AttachmentContract> {
        override val descriptor: SerialDescriptor
            get() = StringDescriptor.withName("Attachment")

        /**
         * This is default deserialize, because it not needed.
         */
        override fun deserialize(decoder: Decoder): AttachmentContract {
            return AttachmentKeyboard("", EMPTY_INLINE_KEYBOARD)
        }

        override fun serialize(encoder: Encoder, obj: AttachmentContract) {
            when (obj) {
                is AttachmentPhoto -> AttachmentPhoto.serializer().serialize(encoder, obj)
                is AttachmentPhotoWithToken -> AttachmentPhotoWithToken.serializer().serialize(encoder, obj)
                is AttachmentPhotoWithUrl -> AttachmentPhotoWithUrl.serializer().serialize(encoder, obj)
                is AttachmentMediaWithUploadData -> AttachmentMediaWithUploadData.serializer().serialize(encoder, obj)
                is AttachmentContact -> AttachmentContact.serializer().serialize(encoder, obj)
                is AttachmentSticker -> AttachmentSticker.serializer().serialize(encoder, obj)
                is AttachmentKeyboard -> AttachmentKeyboard.serializer().serialize(encoder, obj)
                is AttachmentLocation -> AttachmentLocation.serializer().serialize(encoder, obj)
            }
        }

    }
}

@Serializable
class AttachmentKeyboard(
    val type: String,
    val payload: InlineKeyboard
) : AttachmentContract()

@Serializable
class AttachmentContact(
    val type: String,
    val payload: PayloadContact
) : AttachmentContract()

@Serializable
class AttachmentSticker(
    val type: String,
    val payload: PayloadSticker
) : AttachmentContract()

@Serializable
class AttachmentPhoto(
    val type: String,
    val payload: UploadInfo
) : AttachmentContract()

@Serializable
class AttachmentPhotoWithToken(
    val type: String,
    val payload: PayloadToken
) : AttachmentContract()

@Serializable
class AttachmentMediaWithUploadData(
    val type: String,
    val payload: UploadInfo
) : AttachmentContract()

@Serializable
class AttachmentPhotoWithUrl(
    val type: String,
    val payload: PayloadUrl
) : AttachmentContract()

@Serializable
class AttachmentLocation(
    val type: String,
    val latitude: Double,
    val longitude: Double
) : AttachmentContract()

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