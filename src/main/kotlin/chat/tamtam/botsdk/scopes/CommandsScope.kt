package chat.tamtam.botsdk.scopes

import chat.tamtam.botsdk.client.RequestsManager
import chat.tamtam.botsdk.client.ResultSend
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.request.AttachmentKeyboard
import chat.tamtam.botsdk.model.request.InlineKeyboard
import chat.tamtam.botsdk.model.request.SendMessage
import chat.tamtam.botsdk.model.response.AttachType
import chat.tamtam.botsdk.state.CommandState
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@BotMarker
class CommandsScope(
    val requests: RequestsManager,
    private val log: Logger = LoggerFactory.getLogger(CommandsScope::class.java.name),
    private var unknownCommandAction: suspend (CommandState) -> Unit = {},
    private val commandsAnswers: MutableMap<String, suspend (CommandState) -> Unit> = mutableMapOf()
) {

    internal operator fun get(command: String) = commandsAnswers[command] ?: unknownCommandAction

    private fun saveCommand(command: String, action: suspend (CommandState) -> Unit) {
        log.info("saveCommand: start save command -> $command")
        commandsAnswers[command] = action
    }

    private fun saveUnknownCommand(action: suspend (CommandState) -> Unit) {
        log.info("saveCommand: start save unknown command")
        unknownCommandAction = action
    }

//    fun onStartCommand(action: suspend (CommandState) -> Unit) {
//        saveCommand("/start", action)
//        log.info("onStartCommand: saved")
//    }

    fun onCommand(command: String, action: suspend (CommandState) -> Unit) {
        saveCommand(command, action)
        log.info("onCommand: saved command -> $command")
    }

    fun onUnknownCommand(action: suspend (CommandState) -> Unit) {
        saveUnknownCommand(action)
        log.info("onUnknownCommand: saved")
    }

    private suspend fun send(userId: UserId, sendMessage: SendMessage): ResultSend = requests.send(userId, sendMessage)

    suspend infix fun SendMessage.sendFor(userId: UserId): ResultSend = send(userId, this)

    suspend infix fun String.sendFor(userId: UserId): ResultSend = SendMessage(this).sendFor(userId)

    infix fun String.prepareFor(userId: UserId): Pair<UserId, SendMessage> {
        return Pair(userId, SendMessage(this))
    }

    suspend infix fun Pair<UserId, SendMessage>.sendWith(keyboard: InlineKeyboard) {
        val attaches = listOf(AttachmentKeyboard(AttachType.INLINE_KEYBOARD.value.toLowerCase(), keyboard))
        send(first, SendMessage(second.text, attaches, second.notifyUser))
    }
}