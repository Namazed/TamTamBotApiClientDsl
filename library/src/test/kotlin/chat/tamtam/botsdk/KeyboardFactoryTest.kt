package chat.tamtam.botsdk

import chat.tamtam.botsdk.keyboard.keyboard
import chat.tamtam.botsdk.model.Button
import chat.tamtam.botsdk.model.ButtonType
import org.junit.jupiter.api.Test

class KeyboardFactoryTest {

    @Test
    fun `checking that the keyboard is being created correctly using unary plus`() {
        val inlineKeyboard = keyboard {
            +buttonRow {
                +Button(ButtonType.CALLBACK.value, "test1")
                +Button(ButtonType.CALLBACK.value, "test2")
            }
            +buttonRow {
                +Button(ButtonType.CALLBACK.value, "test3")
            }
        }

        assert(inlineKeyboard.buttons.size == 2)
        assert(inlineKeyboard.buttons[0].size == 2)
        assert(inlineKeyboard.buttons[1].size == 1)
    }

    @Test
    fun `checking that the keyboard is being created correctly using add function`() {
        val inlineKeyboard = keyboard {
            this add buttonRow {
                this add Button(ButtonType.CALLBACK.value, "test1")
                this add Button(ButtonType.CALLBACK.value, "test2")
            }
            this add buttonRow {
                this add Button(ButtonType.CALLBACK.value, "test3")
                this add Button(ButtonType.CALLBACK.value, "test4")
                this add Button(ButtonType.CALLBACK.value, "test5")
            }
        }

        assert(inlineKeyboard.buttons.size == 2)
        assert(inlineKeyboard.buttons[0].size == 2)
        assert(inlineKeyboard.buttons[1].size == 3)
    }
}