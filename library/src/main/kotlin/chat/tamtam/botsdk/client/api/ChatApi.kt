package chat.tamtam.botsdk.client.api

import chat.tamtam.botsdk.client.BOT_TOKEN_FIELD
import chat.tamtam.botsdk.client.VERSION_FIELD
import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.request.ActionWrapper
import chat.tamtam.botsdk.model.request.ChatInfo
import chat.tamtam.botsdk.model.response.Chat
import chat.tamtam.botsdk.model.response.ChatMember
import chat.tamtam.botsdk.model.response.ChatMembersResult
import chat.tamtam.botsdk.model.response.ChatsResult
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

private const val CHATS_ENDPOINT = "/chats"

interface ChatApi {

    @GET(CHATS_ENDPOINT)
    fun getChats(
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query(VERSION_FIELD) version: String,
        @Query("count") count: Int = 50,
        @Query("marker") marker: Long? = null
    ): Call<ChatsResult>

    @GET("$CHATS_ENDPOINT/{chatId}")
    fun getChat(
        @Path("chatId") chatId: ChatId,
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query(VERSION_FIELD) version: String
    ): Call<Chat>

    @GET("$CHATS_ENDPOINT/{chatId}/members/me")
    fun getMembershipInfo(
        @Path("chatId") chatId: ChatId,
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query(VERSION_FIELD) version: String
    ): Call<ChatMember>

    @PATCH("$CHATS_ENDPOINT/{chatId}")
    fun editChatInfo(
        @Path("chatId") chatId: ChatId,
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query(VERSION_FIELD) version: String,
        @Body chatInfo: ChatInfo
    ): Call<Chat>

    @POST("$CHATS_ENDPOINT/{chatId}/actions")
    fun sendAction(
        @Path("chatId") chatId: ChatId,
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query(VERSION_FIELD) version: String,
        @Body action: ActionWrapper
    ): Call<Default>

    @DELETE("$CHATS_ENDPOINT/{chatId}/members/me")
    fun leaveChat(
        @Path("chatId") chatId: ChatId,
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query(VERSION_FIELD) version: String
    ): Call<Default>

    @GET("$CHATS_ENDPOINT/{chatId}/members")
    fun getMembers(
        @Path("chatId") chatId: ChatId,
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query(VERSION_FIELD) version: String,
        @Query("count") count: Int = 20,
        @Query("marker") marker: Long? = null,
        @Query("user_ids") userIds: List<Long>? = null
    ): Call<ChatMembersResult>

    @FormUrlEncoded
    @POST("$CHATS_ENDPOINT/{chatId}/members")
    fun addMembers(
        @Path("chatId") chatId: ChatId,
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query(VERSION_FIELD) version: String,
        @Query("user_ids") userIds: List<Long>
    ): Call<Default>

    @DELETE("$CHATS_ENDPOINT/{chatId}/members")
    fun removeMember(
        @Path("chatId") chatId: ChatId,
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query(VERSION_FIELD) version: String,
        @Query("user_id") userId: UserId
    ): Call<Default>
}
