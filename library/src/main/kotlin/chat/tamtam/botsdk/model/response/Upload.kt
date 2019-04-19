package chat.tamtam.botsdk.model.response

import kotlinx.serialization.Serializable

/**
 * This class you will get if you call request [chat.tamtam.botsdk.client.RequestsManager.getUploadUrl]
 *
 * @param url - url for uploading [chat.tamtam.botsdk.model.request.AttachmentContract]
 */
@Serializable
class Upload(
    val url: String
)