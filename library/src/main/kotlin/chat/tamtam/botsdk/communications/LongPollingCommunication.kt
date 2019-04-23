package chat.tamtam.botsdk.communications

import chat.tamtam.botsdk.UpdatesHandler
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

    override fun start(botScope: BotScope, async: Boolean) {
        val updateParsing = UpdatesHandler(botScope)
        if (async) {
            runAsync(updateParsing)
        } else {
            run(updateParsing)
        }
    }

    private fun runAsync(updatesHandler: UpdatesHandler) {
        longPollingCoroutineScope.launch {
            while (isActive) {
                updatesHandler.run()
            }
        }
    }

    private fun run(updatesHandler: UpdatesHandler) {
        runBlocking {
            while (isActive) {
                updatesHandler.run()
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
     * @param httpLogsEnabled - this flag mean that you want see http logs (when send some request).
     * The SocketTimeoutException in httpLogs is normal for longPolling.
     */
    operator fun invoke(botToken: String, async: Boolean = false, httpLogsEnabled: Boolean = false, init: BotScope.() -> Unit): BotScope {
        check(botToken.isNotEmpty()) { "Bot token must is not empty" }
        val longPollingCommunication = LongPollingCommunication(botToken)
        val botHttpManager = HttpManager(longPollingCommunication.botToken, httpLogsEnabled)
        return longPollingCommunication.init(botHttpManager, longPollingCommunication.log, async, init)
    }
}

private fun Communication.init(botHttpManager: HttpManager, log: Logger, async: Boolean, init: BotScope.() -> Unit): BotScope {
    log.info("Long polling bot starting")
    val scope = BotScope(botHttpManager)
    scope.init()
    start(scope, async)
    return scope
}
