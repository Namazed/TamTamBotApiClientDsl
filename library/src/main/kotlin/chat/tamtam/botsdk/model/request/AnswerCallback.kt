package chat.tamtam.botsdk.model.request

import chat.tamtam.botsdk.model.AttachType
import chat.tamtam.botsdk.model.ImageUrl
import chat.tamtam.botsdk.model.response.UploadInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This class need for answer user as replacement message (edit old message with keyboard) on button click (Callback by payload)
 *
 * @param message - message which replace old message (with keyboard) where user click on button
 */
@Serializable
class AnswerCallback(
    val message: SendMessage
)

/**
 * This class need for answer user as notification(toast) on button click (Callback by payload)
 *
 * @param userId - unique identifier of user to whom you want send notification
 * @param notification - text of notification
 */
@Serializable
class AnswerNotificationCallback(
    @SerialName("user_id") val userId: Long,
    val notification: String
)

internal fun createAnswerCallbackForKeyboard(sendMessage: SendMessage, keyboard: InlineKeyboard): AnswerCallback {
    return AnswerCallback(SendMessage(sendMessage.text, listOf(AttachmentKeyboard(AttachType.INLINE_KEYBOARD.value.toLowerCase(), keyboard)), sendMessage.notifyUser))
}

internal fun createAnswerCallbackForImageUrl(sendMessage: SendMessage, imageUrl: ImageUrl): AnswerCallback {
    return AnswerCallback(SendMessage(sendMessage.text,
        listOf(AttachmentPhotoWithUrl(AttachType.IMAGE.value, PayloadUrl(imageUrl.value))), sendMessage.notifyUser))
}

internal fun createAnswerCallbackForMediaToken(uploadType: UploadType, sendMessage: SendMessage, response: UploadInfo): AnswerCallback {
    return when (uploadType) {
        UploadType.VIDEO -> AnswerCallback(SendMessage(sendMessage.text, listOf(AttachmentMediaWithId(AttachType.VIDEO.value, response)), sendMessage.notifyUser))
        UploadType.FILE -> AnswerCallback(SendMessage(sendMessage.text, listOf(AttachmentMediaWithId(AttachType.FILE.value, response)), sendMessage.notifyUser))
        UploadType.AUDIO -> AnswerCallback(SendMessage(sendMessage.text, listOf(AttachmentMediaWithId(AttachType.FILE.value, response)), sendMessage.notifyUser))
        else -> throw IllegalArgumentException("Incorrect uploadType for this method")
    }
}

internal fun createAnswerCallbackForReusablePhotoToken(sendMessage: SendMessage, reusablePhotoToken: String): AnswerCallback {
    return AnswerCallback(SendMessage(sendMessage.text, listOf(AttachmentPhotoWithToken(AttachType.IMAGE.value, PayloadToken(reusablePhotoToken))), sendMessage.notifyUser))
}

internal fun createAnswerCallbackForImageToken(sendMessage: SendMessage, response: UploadInfo): AnswerCallback {
    return AnswerCallback(SendMessage(sendMessage.text, listOf(AttachmentPhoto(AttachType.IMAGE.value, response)), sendMessage.notifyUser))
}

internal fun createAnswerCallbackForListImageTokens(sendMessage: SendMessage, uploadedTokens: List<UploadInfo>): AnswerCallback {
    return AnswerCallback(SendMessage(sendMessage.text, uploadedTokens.map { AttachmentPhoto(AttachType.IMAGE.value, it) }, sendMessage.notifyUser))
}