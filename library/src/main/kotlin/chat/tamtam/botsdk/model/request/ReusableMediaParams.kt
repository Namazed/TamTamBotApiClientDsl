package chat.tamtam.botsdk.model.request

/**
 * Use this class when you want attach for new message some medias which had already uploaded
 *
 * Use only one of this parameter
 *
 * @param uploadType - type of media which you want upload [UploadType]
 * @param token - token, you will get it after send uploaded image or after upload video/audio/file
 */
class ReusableMediaParams(
    val uploadType: UploadType,
    val token: String? = null
)