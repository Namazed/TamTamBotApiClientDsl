package chat.tamtam.botsdk.communications

import chat.tamtam.botsdk.Coordinator
import chat.tamtam.botsdk.UpdatesCoordinator
import chat.tamtam.botsdk.client.HttpManager
import chat.tamtam.botsdk.scopes.BotScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.coroutines.EmptyCoroutineContext

class LongPollingCommunication(
    val botToken: String,
    private val updatesScope: CoroutineScope = CoroutineScope(EmptyCoroutineContext),
    val log: Logger = LoggerFactory.getLogger(LongPollingCommunication::class.java.name)
) : Communication {

    override fun start(botScope: BotScope): Coordinator {
        val coordinator = UpdatesCoordinator(botScope)
        updatesScope.launch {
            while (isActive) {
                coordinator.run()
            }
        }
        return coordinator
    }

}

/**
 * This class need for start longPolling for your bot.
 */
object longPolling {
    operator fun invoke(botToken: String, init: BotScope.() -> Unit): Coordinator {
        check(botToken.isNotEmpty()) { "Bot token must is not empty" }
        val longPollingCommunication = LongPollingCommunication(botToken)
        val botHttpManager = HttpManager(longPollingCommunication.botToken)
        return longPollingCommunication.init(botHttpManager, longPollingCommunication.log, init)
    }
}

private fun Communication.init(botHttpManager: HttpManager, log: Logger, init: BotScope.() -> Unit): Coordinator {
    log.info("Long polling bot starting")
    val scope = BotScope(botHttpManager)
    scope.init()
    return start(scope)
}
