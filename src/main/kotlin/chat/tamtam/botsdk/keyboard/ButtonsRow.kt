package chat.tamtam.botsdk.keyboard

import chat.tamtam.botsdk.model.Button

class ButtonsRow internal constructor(
    private val row: MutableList<Button> = mutableListOf()
) {

    /**
     * Added button to Row.
     * You can use this function or [add]
     */
    operator fun Button.unaryPlus() {
        row += this
    }

    /**
     * Added button to Row.
     * You can use this function or [unaryPlus]
     */
    infix fun ButtonsRow.add(button: Button) {
        row += button
    }

    internal fun create() = row
}