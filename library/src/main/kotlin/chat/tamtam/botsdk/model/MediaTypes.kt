package chat.tamtam.botsdk.model

import okhttp3.MediaType

internal enum class MediaTypes(val type: MediaType?) {
    IMAGE_MEDIA_TYPE(MediaType.parse("image/*")),
    VIDEO_MEDIA_TYPE(MediaType.parse("video/*")),
    AUDIO_MEDIA_TYPE(MediaType.parse("audio/*")),
    ALL_MEDIA_TYPE(MediaType.parse("all"))
}