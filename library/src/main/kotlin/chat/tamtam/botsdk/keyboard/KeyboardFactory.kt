package chat.tamtam.botsdk.keyboard

import chat.tamtam.botsdk.model.Button
import chat.tamtam.botsdk.model.request.InlineKeyboard
import chat.tamtam.botsdk.scopes.BotMarker

@BotMarker
class KeyboardFactory(
    private val buttons: MutableList<List<Button>> = mutableListOf()
) {

    /**
     * This method created row with buttons
     * @param initRow - this lambda contains all your buttons
     */
    fun buttonRow(initRow: ButtonsRow.() -> Unit): List<Button> {
        val buttonsRow = ButtonsRow()
        buttonsRow.initRow()
        return buttonsRow.create()
    }

    /**
     * Added row of buttons to keyboard.
     * You can use this function or [add]
     */
    operator fun List<Button>.unaryPlus() {
        buttons += this
    }

    /**
     * Added row of buttons to keyboard.
     * You can use this function or [unaryPlus]
     */
    infix fun KeyboardFactory.add(buttonsInRow: List<Button>) {
        buttons += buttonsInRow
    }

    fun create() : List<List<Button>> = buttons

}

/**
 * This method created [InlineKeyboard]
 * When you want create keyboard use this simply method
 * @param init - this lambda contains all your button rows
 */
inline fun keyboard(init: KeyboardFactory.() -> Unit): InlineKeyboard {
    val keyboardFactory = KeyboardFactory()
    keyboardFactory.init()
    return InlineKeyboard(keyboardFactory.create())
}