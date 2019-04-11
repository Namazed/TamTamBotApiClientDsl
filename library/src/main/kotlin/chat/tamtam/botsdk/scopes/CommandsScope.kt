package chat.tamtam.botsdk.scopes

import chat.tamtam.botsdk.client.RequestsManager
import chat.tamtam.botsdk.state.CommandState
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@BotMarker
class CommandsScope internal constructor(
    override val requests: RequestsManager,
    private val log: Logger = LoggerFactory.getLogger(CommandsScope::class.java.name),
    private var unknownCommandAction: suspend (CommandState) -> Unit = {},
    private val commandsAnswers: MutableMap<String, suspend (CommandState) -> Unit> = mutableMapOf()
) : Scope {

    internal operator fun get(command: String) = commandsAnswers[command] ?: unknownCommandAction

    private fun saveCommand(command: String, action: suspend (CommandState) -> Unit) {
        log.info("saveCommand: start save command -> $command")
        commandsAnswers[command] = action
    }

    private fun saveUnknownCommand(action: suspend (CommandState) -> Unit) {
        log.info("saveCommand: start save unknown command")
        unknownCommandAction = action
    }

    /**
     * This method save action which call when [chat.tamtam.botsdk.UpdatesHandler] process new message with specific command.
     *
     * @param command - text of command.
     * @param action - all actions in this lambda is async.
     */
    fun onCommand(command: String, action: suspend (CommandState) -> Unit) {
        saveCommand(command.toLowerCase(), action)
        log.info("onCommand: saved command -> $command")
    }

    /**
     * This method save action which call when [chat.tamtam.botsdk.UpdatesHandler] process new message with unknown command.
     *
     * @param action - all actions in this lambda is async.
     */
    fun onUnknownCommand(action: suspend (CommandState) -> Unit) {
        saveUnknownCommand(action)
        log.info("onUnknownCommand: saved")
    }
}