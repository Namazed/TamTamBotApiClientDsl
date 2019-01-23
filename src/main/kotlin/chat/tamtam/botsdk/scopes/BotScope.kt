package chat.tamtam.botsdk.scopes

import chat.tamtam.botsdk.BotMarker
import chat.tamtam.botsdk.client.BotHttpManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@BotMarker
class BotScope(
    internal val botHttpManager: BotHttpManager,
    private val log: Logger = LoggerFactory.getLogger(BotScope::class.java.name),
    private val commandScope: CommandsScope = CommandsScope(),
    private val callbacksScope: CallbacksScope = CallbacksScope(),
    private val messagesScope: MessagesScope = MessagesScope()
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
        message = "LongPolling can't be nested.")
    fun longPolling(botToken: String, init: () -> Unit = {}) {

    }
}
