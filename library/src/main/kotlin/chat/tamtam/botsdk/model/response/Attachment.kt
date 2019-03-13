package chat.tamtam.botsdk.model.response

import chat.tamtam.botsdk.model.AttachType
import chat.tamtam.botsdk.model.AttachTypeSerializer
import chat.tamtam.botsdk.model.Button
import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

val EMPTY_PAYLOAD = Payload()

/**
 * This object may contains in [SendMessage]
 *
 * @param type - type of attach
 * @param callbackId - Optional. For [AttachType.INLINE_KEYBOARD]. Unique identifier of keyboard
 * @param latitude - Optional. For [AttachType.LOCATION]
 * @param longitude - Optional. For [AttachType.LOCATION]
 * @param payload - Optional. For other attach types.
 */
@Serializable
class Attachment(
    @Serializable(AttachTypeSerializer::class) val type: AttachType,
    @SerialName("callback_id") @Optional val callbackId: String = "",
    @Optional val latitude: Long = -1,
    @Optional val longitude: Long = -1,
    @Optional val payload: Payload = EMPTY_PAYLOAD
)

/**
 * This object may contains in [Attachment]
 *
 * @param photoId - Optional. For [AttachType.IMAGE]. Unique identifier of photo attach
 * @param url - Optional. For media attaches. Url of media (photo, video, etc)
 * @param vcfInfo - Optional. For [AttachType.CONTACT]
 * @param tamInfo - Optional. For [AttachType.CONTACT]. User info
 * @param buttons - Optional. For [AttachType.INLINE_KEYBOARD]
 */
@Serializable
class Payload(
    @SerialName("photo_id") @Optional val photoId: Long = -1,
    @Optional val url: String = "",
    @Optional val vcfInfo: String = "",
    @Optional val tamInfo: User = User(),
    @Optional val buttons: List<List<Button>> = emptyList()
)