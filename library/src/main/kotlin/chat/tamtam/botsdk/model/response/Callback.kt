package chat.tamtam.botsdk.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

val EMPTY_CALLBACK = Callback()

/**
 * This class you get when user pressed on button.
 *
 * @param timestamp - Unix-time when user pressed the button
 * @param callbackId - Current keyboard identifier
 * @param payload - Button payload, which user pressed
 * @param user - User who pressed the button
 */
@Serializable
class Callback(
    val timestamp: Long = -1,
    @SerialName("callback_id") val callbackId: String = "",
    val payload: String = "",
    val user: User = User()
)

internal fun isNotEmptyCallback(callback: Callback?) =
    callback != null && callback.timestamp != -1L