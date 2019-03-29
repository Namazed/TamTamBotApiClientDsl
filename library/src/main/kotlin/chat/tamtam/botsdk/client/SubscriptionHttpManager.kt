package chat.tamtam.botsdk.client

import chat.tamtam.botsdk.client.api.SubscriptionApi
import chat.tamtam.botsdk.client.retrofit.HttpResult
import chat.tamtam.botsdk.client.retrofit.await
import chat.tamtam.botsdk.model.response.Default
import chat.tamtam.botsdk.model.response.Updates
import retrofit2.Retrofit
import chat.tamtam.botsdk.model.request.Subscription as RequestSubscription
import chat.tamtam.botsdk.model.response.Subscription as ResponseSubscription

class SubscriptionHttpManager internal constructor(
    private val botToken: String,
    private val version: String,
    retrofit: Retrofit,
    private val subscriptionService: SubscriptionApi = retrofit.create(SubscriptionApi::class.java)
) {
    suspend fun getSubscriptions(): HttpResult<List<ResponseSubscription>> =
        subscriptionService.getSubscriptions(botToken, version).await()

    suspend fun subscribe(requestSubscription: RequestSubscription): HttpResult<Default> =
        subscriptionService.subscribe(botToken, requestSubscription).await()

    suspend fun unsubscribe(url: String): HttpResult<Default> = subscriptionService.unsubscribe(botToken, version, url).await()

    suspend fun getUpdates(marker: Long?): HttpResult<Updates> = subscriptionService.getUpdates(botToken, version, marker).await()
}


