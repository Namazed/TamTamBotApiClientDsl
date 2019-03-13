package chat.tamtam.botsdk.model.request

import chat.tamtam.botsdk.model.Button
import kotlinx.serialization.Serializable

val EMPTY_INLINE_KEYBOARD = InlineKeyboard()

/**
 * Use this class if you want send keyboard.
 *
 * @param buttons - double list with buttons
 */
@Serializable
class InlineKeyboard(
    val buttons: List<List<Button>> = emptyList()
)