package chat.tamtam.botsdk.client.api

import chat.tamtam.botsdk.client.BOT_TOKEN_FIELD
import chat.tamtam.botsdk.model.response.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

internal interface BotApi {

    @GET("/me")
    fun getBotInfo(
        @Query(BOT_TOKEN_FIELD) botToken: String
    ): Call<User>

}
