package chat.tamtam.botsdk.model.request

enum class UploadType(val type: String) {
    PHOTO("photo"),
    VIDEO("video"),
    AUDIO("audio"),
    FILE("file")
}