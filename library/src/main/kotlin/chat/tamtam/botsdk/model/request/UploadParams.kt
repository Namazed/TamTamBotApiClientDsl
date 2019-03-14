package chat.tamtam.botsdk.model.request

/**
 * Use this class when you want answer on Callback or send message with upload video, audio, photo, file
 *
 * @param path - local path on media which you want upload
 * @param uploadType - type of media which you want upload [UploadType]
 */
class UploadParams(
    val path: String,
    val uploadType: UploadType
)