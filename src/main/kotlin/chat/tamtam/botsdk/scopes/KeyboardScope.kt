package chat.tamtam.botsdk.scopes

import chat.tamtam.botsdk.model.Button
import chat.tamtam.botsdk.model.request.InlineKeyboard

@BotMarker
class KeyboardScope(
    internal val buttons: MutableList<List<Button>> = mutableListOf()
) {

    fun buttonRow(initRow: ButtonsRow.() -> Unit): List<Button> {
        val buttonsRow = ButtonsRow()
        buttonsRow.initRow()
        return buttonsRow.create()
    }

    operator fun List<Button>.unaryPlus() {
        buttons += this
    }

    //todo необходимо сделать internal package, для этого скорее всего нужно будет перенести keyboard метод в каждый scope
    fun create() = buttons

}

inline fun keyboard(init: KeyboardScope.() -> Unit): InlineKeyboard {
    val keyboardScope = KeyboardScope()
    keyboardScope.init()
    return InlineKeyboard(keyboardScope.create())
}