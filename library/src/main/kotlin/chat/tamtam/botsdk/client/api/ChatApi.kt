package chat.tamtam.botsdk.client.api

import chat.tamtam.botsdk.client.BOT_TOKEN_FIELD
import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.request.ActionWrapper
import chat.tamtam.botsdk.model.request.ChatInfo
import chat.tamtam.botsdk.model.response.Chat
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

interface ChatApi {

    @GET("/chats")
    fun getChats(
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query("count") count: Int = 50,
        @Query("marker") marker: Long? = null
    ): Call<ChatsResult>

    @GET("/chats/{chatId}")
    fun getChat(
        @Path("chatId") chatId: ChatId,
        @Query(BOT_TOKEN_FIELD) botToken: String
    ): Call<Chat>

    @PATCH("/chats/{chatId}")
    fun editChatInfo(
        @Path("chatId") chatId: ChatId,
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Body chatInfo: ChatInfo
    ): Call<Chat>

    @POST("/chats/{chatId}/actions")
    fun sendAction(
        @Path("chatId") chatId: ChatId,
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Body action: ActionWrapper
    ): Call<Default>

    @DELETE("/chats/{chatId}/members/me")
    fun leaveChat(
        @Path("chatId") chatId: ChatId,
        @Query(BOT_TOKEN_FIELD) botToken: String
    ): Call<Default>

    @GET("/chats/{chatId}/members")
    fun getMembers(
        @Path("chatId") chatId: ChatId,
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query("count") count: Int = 20,
        @Query("marker") marker: Long? = null
    ): Call<ChatMembersResult>

    @FormUrlEncoded
    @POST("/chats/{chatId}/members")
    fun addMembers(
        @Path("chatId") chatId: ChatId,
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query("user_ids") userIds: List<Long>
    ): Call<Default>

    @DELETE("/chats/{chatId}/members")
    fun removeMember(
        @Path("chatId") chatId: ChatId,
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query("user_id") userId: UserId
    ): Call<Default>
}
