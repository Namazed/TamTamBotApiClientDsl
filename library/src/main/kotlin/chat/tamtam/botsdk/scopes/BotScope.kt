package chat.tamtam.botsdk.scopes

import chat.tamtam.botsdk.client.HttpManager
import chat.tamtam.botsdk.client.RequestsManager
import chat.tamtam.botsdk.state.AddedBotState
import chat.tamtam.botsdk.state.RemovedBotState
import chat.tamtam.botsdk.state.StartedBotState
import chat.tamtam.botsdk.typing.TypingController
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

@BotMarker
class BotScope internal constructor(
    internal val botHttpManager: HttpManager,
    private val typingController: TypingController = TypingController(botHttpManager.chatHttpManager, jobs = ConcurrentHashMap()),
    private val log: Logger = LoggerFactory.getLogger(BotScope::class.java.name),
    override val requests: RequestsManager = RequestsManager(botHttpManager, typingController),
    internal val commandScope: CommandsScope = CommandsScope(requests),
    internal val callbacksScope: CallbacksScope = CallbacksScope(requests),
    internal val messagesScope: MessagesScope = MessagesScope(requests),
    internal val userScope: UserScope = UserScope(requests)
) : Scope {

    internal var answerOnStart: suspend (StartedBotState) -> Unit = {}
    internal var answerOnAdd: suspend (AddedBotState) -> Unit = {}
    internal var answerOnRemove: suspend (RemovedBotState) -> Unit = {}

    /**
     * This method need for start [CommandsScope] where you can handle all your commands
     */
    fun commands(init: CommandsScope.() -> Unit) {
        log.info("commands: init")
        commandScope.init()
    }

    /**
     * This method need for start [CallbacksScope] where you can handle all your callbacks
     */
    fun callbacks(init: CallbacksScope.() -> Unit) {
        log.info("callbacks: init")
        callbacksScope.init()
    }

    /**
     * This method need for start [MessagesScope] where you can handle all messages which sent
     * for your bot or sent for chat where your bot is admin
     */
    fun messages(init: MessagesScope.() -> Unit) {
        log.info("messages: init")
        messagesScope.init()
    }

    /**
     * This method need for start [UserScope] where you can handle when user add to chat or remove from chat
     */
    fun users(init: UserScope.() -> Unit) {
        log.info("botStarted: init")
        userScope.init()
    }

    /**
     * This method need for handle start bot action
     */
    fun onStartBot(answer: suspend (StartedBotState) -> Unit) {
        answerOnStart = answer
    }

    /**
     * This method need for handle when somebody added your bot to chat
     */
    fun onAddBotToChat(answer: suspend (AddedBotState) -> Unit) {
        answerOnAdd = answer
    }

    /**
     * This method need for handle when somebody removed your bot from chat
     */
    fun onRemoveBotFromChat(answer: suspend (RemovedBotState) -> Unit) {
        answerOnRemove = answer
    }

    /**
     * This method need only for simpler job with longPolling.
     * It removes the possibility create nested longPolling scope
     */
    @Deprecated(level = DeprecationLevel.ERROR,
        message = "LongPolling communication can't be nested.")
    fun longPolling(botToken: String, init: () -> Unit = {}) {
    }
}
