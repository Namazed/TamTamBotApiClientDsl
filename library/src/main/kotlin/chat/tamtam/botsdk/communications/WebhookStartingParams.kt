package chat.tamtam.botsdk.communications

import chat.tamtam.botsdk.model.request.Subscription

/**
 * Use this class when you want start longPolling process
 * @param botToken - look in [StartingParams]
 * @param httpLogsEnabled - look in [StartingParams]
 * @param subscription - this class contains info about your subscription, if you already subscribed, you can ignore error after start webhook.
 */
class WebhookStartingParams(
    override val botToken: String,
    override val httpLogsEnabled: Boolean = false,
    val subscription: Subscription
) : StartingParams