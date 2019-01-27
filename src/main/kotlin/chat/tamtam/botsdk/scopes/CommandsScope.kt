package chat.tamtam.botsdk.scopes

import chat.tamtam.botsdk.model.Command
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@BotMarker
class CommandsScope(
    private val log: Logger = LoggerFactory.getLogger(CommandsScope::class.java.name),
    private var unknownCommandAction: suspend (Command) -> Unit = {},
    private val commandsAnswers: MutableMap<String, suspend (Command) -> Unit> = mutableMapOf()
) {

    operator fun get(command: String) = commandsAnswers[command] ?: unknownCommandAction

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
}