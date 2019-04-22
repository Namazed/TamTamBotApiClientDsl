package chat.tamtam.botsdk.communications

import chat.tamtam.botsdk.Coordinator
import chat.tamtam.botsdk.UpdatesCoordinator
import chat.tamtam.botsdk.client.HttpManager
import chat.tamtam.botsdk.scopes.BotScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors

class LongPollingCommunication(
    val botToken: String,
    private val longPollingCoroutineScope: CoroutineScope = CoroutineScope(Executors.newSingleThreadExecutor().asCoroutineDispatcher()),
    val log: Logger = LoggerFactory.getLogger(LongPollingCommunication::class.java.name)
) : Communication {

    override fun start(botScope: BotScope, async: Boolean): Coordinator {
        val coordinator = UpdatesCoordinator(botScope)
        if (async) {
            runAsync(coordinator)
        } else {
            run(coordinator)
        }
        return coordinator
    }

    private fun runAsync(coordinator: Coordinator) {
        longPollingCoroutineScope.launch {
            while (isActive) {
                coordinator.run()
            }
        }
    }

    private fun run(coordinator: Coordinator) {
        runBlocking {
            while (isActive) {
                coordinator.run()
            }
        }
    }

}

/**
 * This class need for start longPolling for your bot.
 */
object longPolling {
    /**
     * @param botToken - token of your bot, you can give it from primeBot in TamTam
     * @param async - this flag mean that longPolling start polling on another single thread
     */
    operator fun invoke(botToken: String, async: Boolean = false, init: BotScope.() -> Unit): Coordinator {
        check(botToken.isNotEmpty()) { "Bot token must is not empty" }
        val longPollingCommunication = LongPollingCommunication(botToken)
        val botHttpManager = HttpManager(longPollingCommunication.botToken)
        return longPollingCommunication.init(botHttpManager, longPollingCommunication.log, async, init)
    }
}

private fun Communication.init(botHttpManager: HttpManager, log: Logger, async: Boolean, init: BotScope.() -> Unit): Coordinator {
    log.info("Long polling bot starting")
    val scope = BotScope(botHttpManager)
    scope.init()
    return start(scope, async)
}
