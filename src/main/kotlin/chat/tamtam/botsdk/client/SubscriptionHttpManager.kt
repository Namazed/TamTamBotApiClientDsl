package chat.tamtam.botsdk.client

import chat.tamtam.botsdk.model.response.Default
import chat.tamtam.botsdk.model.response.Updates
import com.namazed.orthobot.botsdk.client.api.SubscriptionApi
import com.namazed.orthobot.botsdk.client.retrofit.await
import retrofit2.Response
import retrofit2.Retrofit
import chat.tamtam.botsdk.model.request.Subscription as RequestSubscription
import chat.tamtam.botsdk.model.response.Subscription as ResponseSubscription

class SubscriptionHttpManager internal constructor(
    private val botToken: String,
    retrofit: Retrofit,
    private val subscriptionService: SubscriptionApi = retrofit.create(SubscriptionApi::class.java)
) {
    suspend fun getSubscriptions(): Response<List<ResponseSubscription>> {
        return subscriptionService.getSubscriptions(botToken).await()
    }

    suspend fun subscribe(requestSubscription: RequestSubscription): Response<Default> {
        return subscriptionService.subscribe(botToken, requestSubscription).await()
    }

    suspend fun unsubscribe(url: String): Response<Default> {
        return subscriptionService.unsubscribe(botToken, url).await()
    }

    suspend fun getUpdates(): ResultUpdates {
        val response = subscriptionService.getUpdates(botToken).await()
        return if (response.isSuccessful) {
            response.body()?.let {
                ResultUpdates.Success(it)
            } ?: ResultUpdates.Failure(response, "Body is empty")
        } else {
            ResultUpdates.Failure(response, "Response isn't successful")
        }
    }
}

sealed class ResultUpdates {
    class Success(val updates: Updates): ResultUpdates()
    class Failure(val response: Response<Updates>, val message: String): ResultUpdates()
}
