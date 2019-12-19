package chat.tamtam.botsdk.model.request

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.EnumSerializer

/**
 * This is enum for media type which you want upload
 */
@UseExperimental(InternalSerializationApi::class)
@Serializable(EnumSerializer::class)
enum class UploadType(val type: String) {
    PHOTO("photo"),
    VIDEO("video"),
    AUDIO("audio"),
    FILE("file")
}

fun updateTypeFrom(value: String) = when (value) {
    "photo" -> UploadType.PHOTO
    "video" -> UploadType.VIDEO
    "audio" -> UploadType.AUDIO
    "file" -> UploadType.FILE
    else -> throw IllegalArgumentException("Unknown type")
}