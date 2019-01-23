package chat.tamtam.botsdk.scopes

import chat.tamtam.botsdk.BotMarker
import chat.tamtam.botsdk.model.Command
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@BotMarker
class CommandsScope(
    private val log: Logger = LoggerFactory.getLogger(CommandsScope::class.java.name),
    private var unknownCommandAction: (Command) -> Unit = { _: Command -> },
    private val commandsMap: MutableMap<Command, (Command) -> Unit> = mutableMapOf()
) {

    operator fun get(command: Command) = commandsMap[command] ?: unknownCommandAction

    private fun saveCommand(command: Command, action: (Command) -> Unit) {
        log.info("saveCommand: start save command -> ${command.name}")
        commandsMap[command] = action
    }

    private fun saveUnknownCommand(action: (Command) -> Unit) {
        log.info("saveCommand: start save unknown command")
        unknownCommandAction = action
    }

    fun onStartCommand(action: (Command) -> Unit) {
        saveCommand(Command("/start"), action)
        log.info("onStartCommand: saved")
    }

    fun onCommand(command: Command, action: (Command) -> Unit) {
        saveCommand(command, action)
        log.info("onCommand: saved command -> ${command.name}")
    }

    fun onUnknownCommand(action: (Command) -> Unit) {
        saveUnknownCommand(action)
        log.info("onUnknownCommand: saved")
    }
}