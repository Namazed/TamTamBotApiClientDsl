package chat.tamtam.botsdk.model.response

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

/**
 * You will get it after you uploaded some type of [chat.tamtam.botsdk.model.request.Attachment]
 *
 * @param photos - you will get it if you uploaded [chat.tamtam.botsdk.model.request.UploadType.PHOTO]
 * @param id - you will get it unique identifier if you uploaded [chat.tamtam.botsdk.model.request.UploadType.VIDEO]
 * or [chat.tamtam.botsdk.model.request.UploadType.AUDIO]
 * @param fileId - you will get it unique identifier of file if you uploaded [chat.tamtam.botsdk.model.request.UploadType.FILE]
 */
@Serializable
class UploadInfo(
    @Optional val photos: Map<String, PhotoToken>? = null,
    @Optional val id: Long? = null,
    @Optional val fileId: Long? = null
)