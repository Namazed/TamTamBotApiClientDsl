package chat.tamtam.botsdk.scopes

import chat.tamtam.botsdk.client.BotHttpManager
import chat.tamtam.botsdk.model.Command
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.request.SendMessage
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@BotMarker
class CommandsScope(
    private val botHttpManager: BotHttpManager,
    private val log: Logger = LoggerFactory.getLogger(CommandsScope::class.java.name),
    private var unknownCommandAction: suspend (Command) -> Unit = {},
    private val commandsAnswers: MutableMap<String, suspend (Command) -> Unit> = mutableMapOf()
) {

    internal operator fun get(command: String) = commandsAnswers[command] ?: unknownCommandAction

    private fun saveCommand(command: String, action: suspend (Command) -> Unit) {
        log.info("saveCommand: start save command -> $command")
        commandsAnswers[command] = action
    }

    private fun saveUnknownCommand(action: suspend (Command) -> Unit) {
        log.info("saveCommand: start save unknown command")
        unknownCommandAction = action
    }

    fun onStartCommand(action: suspend (Command) -> Unit) {
        saveCommand("/start", action)
        log.info("onStartCommand: saved")
    }

    fun onCommand(command: String, action: suspend (Command) -> Unit) {
        saveCommand(command, action)
        log.info("onCommand: saved command -> $command")
    }

    fun onUnknownCommand(action: suspend (Command) -> Unit) {
        saveUnknownCommand(action)
        log.info("onUnknownCommand: saved")
    }

    suspend infix fun Command.send(sendMessage: SendMessage) {
        botHttpManager.sendMessage(UserId(this.update.message.sender.userId), sendMessage)
    }

    suspend infix fun Command.sendText(text: String) {
        send(SendMessage(text))
    }
}