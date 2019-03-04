package chat.tamtam.botsdk.communications

import chat.tamtam.botsdk.UpdatesHandler
import chat.tamtam.botsdk.client.HttpManager
import chat.tamtam.botsdk.scopes.BotScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LongPollingCommunication(
    val botToken: String,
    val log: Logger = LoggerFactory.getLogger(LongPollingCommunication::class.java.name)
) : Communication {

    override fun start(botScope: BotScope) {
        val updateParsing = UpdatesHandler(botScope)
        GlobalScope.launch {
            while (isActive) {
                updateParsing.run()
            }
        }
    }

}

/**
 * This class need for start longPolling for your bot.
 */
object longPolling {
    operator fun invoke(botToken: String, init: BotScope.() -> Unit): BotScope {
        check(botToken.isNotEmpty()) { "Bot token must is not empty" }
        val longPollingCommunication = LongPollingCommunication(botToken)
        val botHttpManager = HttpManager(longPollingCommunication.botToken)
        return longPollingCommunication.init(botHttpManager, longPollingCommunication.log, init)
    }
}

private fun Communication.init(botHttpManager: HttpManager, log: Logger, init: BotScope.() -> Unit): BotScope {
    log.info("Long polling bot starting")
    val scope = BotScope(botHttpManager)
    scope.init()
    start(scope)
    return scope
}
