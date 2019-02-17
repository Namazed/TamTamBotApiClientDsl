package chat.tamtam.botsdk.keyboard

import chat.tamtam.botsdk.model.Button
import chat.tamtam.botsdk.model.request.InlineKeyboard
import chat.tamtam.botsdk.scopes.BotMarker

@BotMarker
class KeyboardFactory(
    private val buttons: MutableList<List<Button>> = mutableListOf()
) {

    fun buttonRow(initRow: ButtonsRow.() -> Unit): List<Button> {
        val buttonsRow = ButtonsRow()
        buttonsRow.initRow()
        return buttonsRow.create()
    }

    operator fun List<Button>.unaryPlus() {
        buttons += this
    }

    infix fun KeyboardFactory.add(buttonsInRow: List<Button>) {
        buttons += buttonsInRow
    }

    //todo необходимо сделать internal package, для этого скорее всего нужно будет перенести keyboard метод в каждый scope
    fun create() = buttons

}

inline fun keyboard(init: KeyboardFactory.() -> Unit): InlineKeyboard {
    val keyboardFactory = KeyboardFactory()
    keyboardFactory.init()
    return InlineKeyboard(keyboardFactory.create())
}