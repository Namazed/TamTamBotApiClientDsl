package com.namazed.orthobot.botsdk.client.api

import chat.tamtam.botsdk.client.BOT_TOKEN_FIELD
import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.MessageId
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.response.Message
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import chat.tamtam.botsdk.model.request.SendMessage as RequestSendMessage
import chat.tamtam.botsdk.model.response.SendMessage as ResponseSendMessage

interface MessageApi {

    @GET("/messages")
    fun getMessages(
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query("count") count: Int = 50,
        @Query("from") fromTime: Long? = null,
        @Query("to") toTime: Long? = null,
        @Query("chat_id") chatId: ChatId
    ): Call<List<Message>>

    @POST("/messages")
    fun sendMessage(
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query("user_id") userId: UserId,
        @Body sendMessage: RequestSendMessage
    ): Call<ResponseSendMessage>

    @POST("/messages")
    fun sendMessage(
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query("chat_id") chatId: ChatId,
        @Body sendMessage: RequestSendMessage
    ): Call<ResponseSendMessage>

    @PUT("/chats/{chatId}")
    fun editMessage(
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query("message_id") messageId: MessageId,
        @Body sendMessage: RequestSendMessage
    ): Call<ResponseSendMessage>

}
