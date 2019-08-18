package chat.tamtam.botsdk.client.api

import chat.tamtam.botsdk.client.BOT_TOKEN_FIELD
import chat.tamtam.botsdk.client.VERSION_FIELD
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query
import chat.tamtam.botsdk.model.request.Bot as RequestBot
import chat.tamtam.botsdk.model.response.Bot as ResponseBot

internal interface BotApi {

    @GET("/me")
    fun getBotInfo(
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query(VERSION_FIELD) version: String
    ): Call<ResponseBot>

    @PATCH("/me")
    fun editBotInfo(
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query(VERSION_FIELD) version: String,
        @Body bot: RequestBot
    ): Call<ResponseBot>

}
