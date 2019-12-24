package chat.tamtam.botsdk.communications

import chat.tamtam.botsdk.Coordinator
import chat.tamtam.botsdk.UpdatesCoordinator
import chat.tamtam.botsdk.client.HttpManager
import chat.tamtam.botsdk.client.ResultRequest
import chat.tamtam.botsdk.scopes.BotScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class WebhookCommunication internal constructor(
    val botToken: String,
    private val subscriptionScope: CoroutineScope = CoroutineScope(Dispatchers.IO),
    val log: Logger = LoggerFactory.getLogger(WebhookCommunication::class.java.name)
) : Communication {
    override fun start(botScope: BotScope, startingParams: StartingParams): Coordinator {
        subscribeIfNeeded(botScope)
        return UpdatesCoordinator(botScope)
    }

    private fun subscribeIfNeeded(botScope: BotScope) {
        subscriptionScope.launch {
            when(val result = botScope.requests.subscribe(botScope.subscription)) {
                is ResultRequest.Success -> log.info("Subscribed with success")
                is ResultRequest.Failure -> log.error("""Subscribed with failure.
                |HttpCode: ${result.httpStatusCode}
                |ErrorCode: ${result.error?.code}
                |ErrorMessage: ${result.error?.message}""".trimMargin(), result.exception)
            }
        }
    }

}

/**
 * This class need for start webhook for your bot.
 */
object webhook {
    operator fun invoke(botToken: String, startingParams: StartingParams, init: BotScope.() -> Unit): Coordinator {
        check(startingParams is WebhookStartingParams) {
            "Wrong startingParams, for webhook you must use WebhookStartingParams"
        }
        check(botToken.isNotEmpty()) { "Bot token must is not empty" }
        check(startingParams.subscription.url.isNotEmpty()) { "Your url must is not empty" }
        val webHookCommunication = WebhookCommunication(botToken)
        val botHttpManager = HttpManager(webHookCommunication.botToken)
        return webHookCommunication.init(botHttpManager, startingParams, webHookCommunication.log, init)
    }
}

private fun Communication.init(
    botHttpManager: HttpManager,
    startingParams: WebhookStartingParams,
    log: Logger,
    init: BotScope.() -> Unit
): Coordinator {
    log.info("Webhook bot starting")
    val scope = BotScope(botHttpManager, startingParams.subscription)
    scope.init()
    return start(scope, startingParams)
}
