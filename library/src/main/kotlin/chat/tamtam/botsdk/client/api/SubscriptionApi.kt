package chat.tamtam.botsdk.client.api

import chat.tamtam.botsdk.client.BOT_TOKEN_FIELD
import chat.tamtam.botsdk.client.VERSION_FIELD
import chat.tamtam.botsdk.model.response.Default
import chat.tamtam.botsdk.model.response.Updates
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import chat.tamtam.botsdk.model.request.Subscription as RequestSubscription
import chat.tamtam.botsdk.model.response.Subscription as ResponseSubscription

private const val SUBSCRIPTIONS_ENDPOINT = "/subscriptions"

internal interface SubscriptionApi {

    @GET(SUBSCRIPTIONS_ENDPOINT)
    fun getSubscriptions(
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query(VERSION_FIELD) version: String
    ): Call<List<ResponseSubscription>>

    @POST(SUBSCRIPTIONS_ENDPOINT)
    fun subscribe(
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Body requestSubscription: RequestSubscription
    ): Call<Default>

    @FormUrlEncoded
    @DELETE(SUBSCRIPTIONS_ENDPOINT)
    fun unsubscribe(
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query(VERSION_FIELD) version: String,
        @Query("url") url: String
    ): Call<Default>

    @GET("/updates")
    fun getUpdates(
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query(VERSION_FIELD) version: String,
        @Query("marker") marker: Long?
    ): Call<Updates>

}
