package chat.tamtam.botsdk.model.prepared

import chat.tamtam.botsdk.model.AttachType
import chat.tamtam.botsdk.model.Button
import chat.tamtam.botsdk.model.CallbackId
import chat.tamtam.botsdk.model.response.SendMessage

/**
 * This object may contains in [SendMessage]
 *
 * @property type - type of attach
 */
interface Attachment {
    val type: AttachType
}

inline fun <reified T> Attachment.cast(): T? {
    return this as? T
}

inline fun <reified T> List<Attachment>.findFirst(): T? {
    return this.first { it is T }.cast()
}

/**
 * This object may contains in [SendMessage]
 *
 * @param latitude - For [AttachType.LOCATION]
 * @param longitude - For [AttachType.LOCATION]
 */
data class AttachmentLocation(
    override val type: AttachType,
    val latitude: Double,
    val longitude: Double
) : Attachment

/**
 * This object may contains in [SendMessage]
 *
 * @param callbackId - For [AttachType.INLINE_KEYBOARD]. Unique identifier of keyboard
 * @param payload - see [PayloadKeyboard]
 */
data class AttachmentKeyboard(
    override val type: AttachType,
    val callbackId: CallbackId,
    val payload: PayloadKeyboard
) : Attachment

/**
 * This object may contains in [SendMessage]
 *
 * @param payload - see [PayloadPhoto]
 */
data class AttachmentPhoto(
    override val type: AttachType,
    val payload: PayloadPhoto
) : Attachment

/**
 * This object may contains in [SendMessage]
 *
 * @param payload - see [PayloadMedia].
 */
data class AttachmentMedia(
    override val type: AttachType,
    val payload: PayloadMedia
) : Attachment

/**
 * This object may contains in [SendMessage]
 *
 * @param payload - see [PayloadContact].
 */
data class AttachmentContact(
    override val type: AttachType,
    val payload: PayloadContact
) : Attachment

/**
 * This object may contains in [Attachment]
 *
 * @param vcfInfo - Optional. For [AttachType.CONTACT]
 * @param tamInfo - Optional. For [AttachType.CONTACT]. User info
 */
data class PayloadContact(
    val vcfInfo: String,
    val tamInfo: User
)

/**
 * This object may contains in [Attachment]
 *
 * @param photoId - Optional. For [AttachType.IMAGE]. Unique identifier of photo attach
 * @param token - Optional. For [AttachType.IMAGE]. Reusable token. It allows to attach the same photo more than once.
 * @param url - Optional. For media attaches. Url of photo
 */
data class PayloadPhoto(
    val photoId: Long,
    val token: String,
    val url: String
)

/**
 * This object may contains in [Attachment]
 *
 * @param url - For media attaches. Url of media (video, audio, etc)
 */
data class PayloadMedia(
    val url: String
)

/**
 * This object may contains in [Attachment]
 *
 * @param buttons - Optional. For [AttachType.INLINE_KEYBOARD]
 */
data class PayloadKeyboard(
    val buttons: List<List<Button>>
)