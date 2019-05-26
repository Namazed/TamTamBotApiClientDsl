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

    override fun start(botScope: BotScope, startingParams: StartingParams) {
        val updateParsing = UpdatesHandler(botScope)
        if (startingParams.async) {
            runAsync(updateParsing, startingParams.parallelWorkWithUpdates)
        } else {
            run(updateParsing, startingParams.parallelWorkWithUpdates)
        }
    }

    private fun runAsync(updatesHandler: UpdatesHandler, parallelWorkWithUpdates: Boolean) =
        longPollingCoroutineScope.launch {
            while (isActive) {
                updatesHandler.run(parallelWorkWithUpdates)
            }
        }

    private fun run(updatesHandler: UpdatesHandler, parallelWorkWithUpdates: Boolean) = runBlocking {
        while (isActive) {
            updatesHandler.run(parallelWorkWithUpdates)
        }
    }

}

/**
 * This class need for start longPolling for your bot.
 */
object longPolling {
    operator fun invoke(startingParams: StartingParams, init: BotScope.() -> Unit): BotScope {
        check(startingParams.botToken.isNotEmpty()) { "Bot token must is not empty" }
        val longPollingCommunication = LongPollingCommunication(startingParams.botToken)
        val botHttpManager = HttpManager(longPollingCommunication.botToken, startingParams.httpLogsEnabled)
        return longPollingCommunication.init(botHttpManager, longPollingCommunication.log, startingParams, init)
    }
}

private fun Communication.init(
    botHttpManager: HttpManager,
    log: Logger, startingParams:
    StartingParams,
    init: BotScope.() -> Unit
): BotScope {
    log.info("Long polling bot starting")
    val scope = BotScope(botHttpManager)
    scope.init()
    start(scope, startingParams)
    return scope
}
