package chat.tamtam.botsdk

import chat.tamtam.botsdk.model.isCommand
import chat.tamtam.botsdk.model.isCommandInChat
import chat.tamtam.botsdk.model.response.ChatType
import chat.tamtam.botsdk.model.response.Message
import chat.tamtam.botsdk.model.response.MessageInfo
import chat.tamtam.botsdk.model.response.Recipient
import chat.tamtam.botsdk.model.response.Update
import chat.tamtam.botsdk.model.response.UpdateType
import chat.tamtam.botsdk.model.toCommand
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CommandTest {

    @Test
    fun `check parse wrong command from text in Dialog`() {
        val textWithCommand = "/!!start"
        assert(!textWithCommand.isCommand()) {
            "This text = $textWithCommand - is not command"
        }
    }

    @Test
    fun `check parse command from text in Dialog`() {
        val textWithCommand = "/start"
        assert(textWithCommand.isCommand())
    }

    @Test
    fun `check parse wrong command from text in Chat`() {
        val textWithCommand = "/start"
        assert(!textWithCommand.isCommandInChat()) {
            "This text = $textWithCommand - is not command"
        }
    }

    @Test
    fun `check parse command from text in Chat`() {
        val textWithCommand = "@bot /start"
        assert(textWithCommand.isCommandInChat())
    }

    @Test
    fun `check parse command with argument from text in Dialog`() {
        val textWithCommand = "/start argument"
        assert(textWithCommand.isCommand())
    }

    @Test
    fun `check parse command with argument from text in Chat`() {
        val textWithCommand = "@bot /start argument"
        assert(textWithCommand.isCommandInChat())
    }

    @Test
    fun `check create command from text in Dialog`() {
        val argument = "argument"
        val commandName = "/start"
        val textWithCommand = "$commandName $argument"
        val update = Update(-1L, UpdateType.MESSAGE_CREATED, message = Message(
            recipient = Recipient(chatType = ChatType.DIALOG), messageInfo = MessageInfo(text = textWithCommand)))
        val command = textWithCommand.toCommand(update)
        assertEquals(commandName, command.name)
        assertEquals(argument, command.argument)
    }

    @Test
    fun `check create command from text in Chat`() {
        val argument = "argument"
        val commandName = "@bot /start"
        val textWithCommand = "$commandName $argument"
        val update = Update(-1L, UpdateType.MESSAGE_CREATED, message = Message(
            recipient = Recipient(chatType = ChatType.CHAT), messageInfo = MessageInfo(text = textWithCommand)))
        val command = textWithCommand.toCommand(update)
        assertEquals(commandName, command.name)
        assertEquals(argument, command.argument)
    }
}