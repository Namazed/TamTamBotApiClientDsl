package com.namazed.orthobot.botsdk.client.api

import chat.tamtam.botsdk.client.BOT_TOKEN_FIELD
import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.request.ActionWrapper
import chat.tamtam.botsdk.model.request.ChatInfo
import chat.tamtam.botsdk.model.response.Chat
import chat.tamtam.botsdk.model.response.ChatMembersResult
import chat.tamtam.botsdk.model.response.Default
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatApi {

    @GET("/chats")
    fun getChats(
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query("count") count: Int = 50,
        @Query("marker") marker: Long? = null
    ): Call<List<Chat>>

    @GET("/chats/{chatId}")
    fun getChat(
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Path("chatId") chatId: ChatId
    ): Call<Chat>

    @FormUrlEncoded
    @PATCH("/chats/{chatId}")
    fun editChatInfo(
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Path("chatId") chatId: ChatId,
        @Body chatInfo: ChatInfo
    ): Call<Chat>

    @FormUrlEncoded
    @POST("/chats/{chatId}/actions")
    fun sendAction(
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Path("chatId") chatId: ChatId,
        @Body action: ActionWrapper
    ): Call<Default>

    @FormUrlEncoded
    @DELETE("/chats/{chatId}/members/me")
    fun leaveChat(
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Path("chatId") chatId: ChatId
    ): Call<Default>

    @GET("/chats/{chatId}/members")
    fun getMembers(
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query("count") count: Int = 20,
        @Path("chatId") chatId: ChatId,
        @Query("marker") marker: Long? = null
    ): Call<ChatMembersResult>

    @FormUrlEncoded
    @POST("/chats/{chatId}/members")
    fun addMembers(
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query("user_ids") userIds: List<Long>,
        @Path("chatId") chatId: ChatId
    ): Call<Default>

    @FormUrlEncoded
    @DELETE("/chats/{chatId}/members")
    fun removeMember(
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query("user_id") userId: UserId,
        @Path("chatId") chatId: ChatId
    ): Call<Default>
}
