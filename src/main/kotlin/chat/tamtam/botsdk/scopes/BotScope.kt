package chat.tamtam.botsdk.scopes

import chat.tamtam.botsdk.client.BotHttpManager
import chat.tamtam.botsdk.client.RequestsManager
import chat.tamtam.botsdk.state.AddedBotState
import chat.tamtam.botsdk.state.RemovedBotState
import chat.tamtam.botsdk.state.StartedBotState
import chat.tamtam.botsdk.typing.TypingController
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@BotMarker
class BotScope(
    internal val botHttpManager: BotHttpManager,
    private val typingController: TypingController = TypingController(botHttpManager),
    private val log: Logger = LoggerFactory.getLogger(BotScope::class.java.name),
    val requests: RequestsManager = RequestsManager(botHttpManager, typingController),
    internal val commandScope: CommandsScope = CommandsScope(requests),
    internal val callbacksScope: CallbacksScope = CallbacksScope(requests),
    internal val messagesScope: MessagesScope = MessagesScope(requests),
    internal val userScope: UserScope = UserScope(requests)
) {

    internal var answerOnStart: suspend (StartedBotState) -> Unit = {}
    internal var answerOnAdd: suspend (AddedBotState) -> Unit = {}
    internal var answerOnRemove: suspend (RemovedBotState) -> Unit = {}

    fun commands(init: CommandsScope.() -> Unit) {
        log.info("commands: init")
        commandScope.init()
    }

    fun callbacks(init: CallbacksScope.() -> Unit) {
        log.info("callbacks: init")
        callbacksScope.init()
    }

    fun messages(init: MessagesScope.() -> Unit) {
        log.info("messages: init")
        messagesScope.init()
    }

    fun users(init: UserScope.() -> Unit) {
        log.info("botStarted: init")
        userScope.init()
    }

    fun onStartBot(answer: suspend (StartedBotState) -> Unit) {
        answerOnStart = answer
    }

    fun onAddBotToChat(answer: suspend (AddedBotState) -> Unit) {
        answerOnAdd = answer
    }

    fun onRemoveBotFromChat(answer: suspend (RemovedBotState) -> Unit) {
        answerOnRemove = answer
    }

    @Deprecated(level = DeprecationLevel.ERROR,
        message = "LongPolling communication can't be nested.")
    fun longPolling(botToken: String, init: () -> Unit = {}) {
    }
}
