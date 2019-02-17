package chat.tamtam.botsdk.model.request

import kotlinx.serialization.Serializable

interface Attachment

@Serializable
class AttachmentKeyboard(
    val type: String,
    val payload: InlineKeyboard
) : Attachment

@Serializable
class AttachmentPhoto(
    val type: String,
    val payload: String
) : Attachment

@Serializable
class AttachmentPhotoWithUrl(
    val type: String,
    val payload: Payload
) : Attachment

@Serializable
class AttachmentLocation(
    val type: String,
    val latitude: Double,
    val longitude: Double
) : Attachment

class Payload(
    val url: String
)