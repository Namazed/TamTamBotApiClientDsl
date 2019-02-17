package chat.tamtam.botsdk.keyboard

import chat.tamtam.botsdk.model.Button

class ButtonsRow(
    internal val row: MutableList<Button> = mutableListOf()
) {

    operator fun Button.unaryPlus() {
        row += this
    }

    infix fun ButtonsRow.add(button: Button) {
        row += button
    }

    internal fun create() = row
}