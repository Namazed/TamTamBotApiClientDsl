package chat.tamtam.botsdk.model.prepared

import chat.tamtam.botsdk.model.CallbackId

/**
 * This class you get when user pressed on button.
 *
 * @param timestamp - Unix-time when user pressed the button
 * @param callbackId - Current keyboard identifier, inline class [CallbackId]
 * @param payload - Button payload, which user pressed
 * @param user - User who pressed the button
 */
data class Callback(
    val timestamp: Long,
    val callbackId: CallbackId,
    val payload: String,
    val user: User
)