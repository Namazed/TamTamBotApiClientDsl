package chat.tamtam.botsdk.model.response

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

@Serializable
class UploadInfo(
    @Optional val photos: Map<String, PhotoToken>? = null,
    @Optional val id: Long? = null,
    @Optional val fileId: Long? = null
)