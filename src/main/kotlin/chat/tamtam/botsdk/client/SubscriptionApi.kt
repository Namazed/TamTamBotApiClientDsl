package chat.tamtam.botsdk.client

import chat.tamtam.botsdk.model.response.Default
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import java.net.URL
import chat.tamtam.botsdk.model.request.Subscription as RequestSubscription
import chat.tamtam.botsdk.model.response.Subscription as ResponseSubscription

class SubscriptionApi internal constructor(
    private val subscriptionApiEndpoint: String,
    private val botToken: String,
    private val httpClient: HttpClient
) {
    suspend fun getSubscriptions() = httpClient.get<List<ResponseSubscription>> {
        url(URL(subscriptionApiEndpoint))
        parameter("access_token", botToken)
    }

    suspend fun subscribe(requestSubscription: RequestSubscription) = httpClient.post<Default> {
        url(URL(subscriptionApiEndpoint))
        parameter("access_token", botToken)
        contentType(ContentType.parse("application/json"))
        body = requestSubscription
    }

    suspend fun unsubscribe(url: String) = httpClient.delete<Default> {
        url(URL(subscriptionApiEndpoint))
        parameter("access_token", botToken)
        parameter("url", url)
    }
}
