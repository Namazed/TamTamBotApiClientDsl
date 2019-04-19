package chat.tamtam.botsdk.model.request

/**
 * Use this class when you want attach for new message some medias which had already uploaded
 *
 * Use only one of this parameter
 *
 * @param uploadType - type of media which you want upload [UploadType]
 * @param photoToken - reusablePhotoToken, you will get it after send uploaded image
 * @param id - reusableId, you will get it after upload video or media
 * @param fileId - reusableFileId, you will get it after upload file
 */
class ReusableMediaParams(
    val uploadType: UploadType,
    val photoToken: String? = null,
    val id: Long? = null,
    val fileId: Long? = null
)