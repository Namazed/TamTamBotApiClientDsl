package chat.tamtam.botsdk.client.api

import chat.tamtam.botsdk.client.BOT_TOKEN_FIELD
import chat.tamtam.botsdk.client.VERSION_FIELD
import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.MessageId
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.response.Default
import chat.tamtam.botsdk.model.response.Messages
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import chat.tamtam.botsdk.model.request.SendMessage as RequestSendMessage
import chat.tamtam.botsdk.model.response.SendMessage as ResponseSendMessage

private const val MESSAGES_ENDPOINT = "/messages"

interface MessageApi {

    @GET(MESSAGES_ENDPOINT)
    fun getMessages(
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query(VERSION_FIELD) version: String,
        @Query("message_ids") message_ids: List<MessageId>? = null,
        @Query("count") count: Int = 50,
        @Query("from") fromTime: Long? = null,
        @Query("to") toTime: Long? = null,
        @Query("chat_id") chatId: ChatId
    ): Call<Messages>

    @DELETE(MESSAGES_ENDPOINT)
    fun deleteMessage(
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query(VERSION_FIELD) version: String,
        @Query("message_id") messageId: MessageId
    ): Call<Default>

    @POST(MESSAGES_ENDPOINT)
    fun sendMessage(
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query(VERSION_FIELD) version: String,
        @Query("user_id") userId: UserId,
        @Body sendMessage: RequestSendMessage
    ): Call<ResponseSendMessage>

    @POST(MESSAGES_ENDPOINT)
    fun sendMessage(
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query(VERSION_FIELD) version: String,
        @Query("chat_id") chatId: ChatId,
        @Body sendMessage: RequestSendMessage
    ): Call<ResponseSendMessage>

    @PUT(MESSAGES_ENDPOINT)
    fun editMessage(
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query(VERSION_FIELD) version: String,
        @Query("message_id") messageId: MessageId,
        @Body sendMessage: RequestSendMessage
    ): Call<Default>

}
