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
    private val longPollingCoroutinesScope: CoroutineScope = CoroutineScope(Executors.newSingleThreadExecutor().asCoroutineDispatcher()),
    val log: Logger = LoggerFactory.getLogger(LongPollingCommunication::class.java.name)
) : Communication {

    override fun start(botScope: BotScope, startingParams: StartingParams): Coordinator {
        check(startingParams is LongPollingStartingParams) {
            "Wrong startingParams, for longPolling you must use LongPollingStartingParams"
        }
        val coordinator = UpdatesCoordinator(botScope)
        if (startingParams.async) {
            runAsync(coordinator, startingParams.parallelWorkWithUpdates)
        } else {
            run(coordinator, startingParams.parallelWorkWithUpdates)
        }
        return coordinator
    }

    private fun runAsync(coordinator: UpdatesCoordinator, parallelWorkWithUpdates: Boolean) =
        longPollingCoroutinesScope.launch {
            while (isActive) {
                coordinator.run(parallelWorkWithUpdates)
            }
        }

    private fun run(coordinator: UpdatesCoordinator, parallelWorkWithUpdates: Boolean) = runBlocking {
        while (isActive) {
            coordinator.run(parallelWorkWithUpdates)
        }
    }

}

/**
 * This class need for start longPolling for your bot.
 */
object longPolling {
    operator fun invoke(startingParams: StartingParams, init: BotScope.() -> Unit): Coordinator {
        check(startingParams.botToken.isNotEmpty()) { "Bot token must is not empty" }
        val longPollingCommunication = LongPollingCommunication(startingParams.botToken)
        val botHttpManager = HttpManager(longPollingCommunication.botToken, startingParams.httpLogsEnabled)
        return longPollingCommunication.init(botHttpManager, startingParams, longPollingCommunication.log, init)
    }
}

private fun Communication.init(
    botHttpManager: HttpManager,
    startingParams: StartingParams,
    log: Logger,
    init: BotScope.() -> Unit
): Coordinator {
    log.info("Long polling bot starting")
    val scope = BotScope(botHttpManager)
    scope.init()
    return start(scope, startingParams)
}
