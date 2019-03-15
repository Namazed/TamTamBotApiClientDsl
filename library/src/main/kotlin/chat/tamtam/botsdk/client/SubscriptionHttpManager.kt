package chat.tamtam.botsdk.client

import chat.tamtam.botsdk.client.retrofit.HttpResult
import chat.tamtam.botsdk.client.retrofit.await
import chat.tamtam.botsdk.model.response.Default
import chat.tamtam.botsdk.model.response.Updates
import chat.tamtam.botsdk.client.api.SubscriptionApi
import retrofit2.Retrofit
import chat.tamtam.botsdk.model.request.Subscription as RequestSubscription
import chat.tamtam.botsdk.model.response.Subscription as ResponseSubscription

class SubscriptionHttpManager internal constructor(
    private val botToken: String,
    retrofit: Retrofit,
    private val subscriptionService: SubscriptionApi = retrofit.create(SubscriptionApi::class.java)
) {
    suspend fun getSubscriptions(): HttpResult<List<ResponseSubscription>> =
        subscriptionService.getSubscriptions(botToken).await()

    suspend fun subscribe(requestSubscription: RequestSubscription): HttpResult<Default> =
        subscriptionService.subscribe(botToken, requestSubscription).await()

    suspend fun unsubscribe(url: String): HttpResult<Default> = subscriptionService.unsubscribe(botToken, url).await()

    suspend fun getUpdates(marker: Long?): HttpResult<Updates> = subscriptionService.getUpdates(botToken, marker).await()
}


