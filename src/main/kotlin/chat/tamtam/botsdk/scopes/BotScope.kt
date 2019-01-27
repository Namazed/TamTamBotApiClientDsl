package chat.tamtam.botsdk.scopes

import chat.tamtam.botsdk.client.BotHttpManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@BotMarker
class BotScope(
    internal val botHttpManager: BotHttpManager,
    private val log: Logger = LoggerFactory.getLogger(BotScope::class.java.name),
    internal val commandScope: CommandsScope = CommandsScope(botHttpManager),
    internal val callbacksScope: CallbacksScope = CallbacksScope(botHttpManager),
    internal val messagesScope: MessagesScope = MessagesScope(botHttpManager)
) {

    fun commands(init: CommandsScope.() -> Unit) {
        log.info("commands: init")
        init(commandScope)
    }

    fun callbacks(init: CallbacksScope.() -> Unit) {
        log.info("callbacks: init")
        init(callbacksScope)
    }

    fun messages(init: MessagesScope.() -> Unit) {
        log.info("messages: init")
        init(messagesScope)
    }

    @Deprecated(level = DeprecationLevel.ERROR,
        message = "LongPolling communication can't be nested.")
    fun longPolling(botToken: String, init: () -> Unit = {}) {
    }
}
