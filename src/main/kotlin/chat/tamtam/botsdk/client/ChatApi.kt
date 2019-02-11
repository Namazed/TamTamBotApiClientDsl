package chat.tamtam.botsdk.client

import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.request.Action
import chat.tamtam.botsdk.model.request.ChatInfo
import chat.tamtam.botsdk.model.response.Chat
import chat.tamtam.botsdk.model.response.ChatMembersResult
import chat.tamtam.botsdk.model.response.ChatsResult
import chat.tamtam.botsdk.model.response.Default
import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import java.net.URL

class ChatApi(
    private val chatApiEndpoint: String,
    private val botToken: String,
    private val httpClient: HttpClient
) {
    suspend fun getAllChats(count: Int = 50, marker: Long? = null) = httpClient.get<ChatsResult> {
        url(URL(chatApiEndpoint))
        parameter("access_token", botToken)
        parameter("count", count)
        parameter("marker", marker)
    }

    suspend fun getChat(chatId: ChatId) = httpClient.get<Chat> {
        url(URL("$chatApiEndpoint/${chatId.id}"))
        parameter("access_token", botToken)
    }

    suspend fun editChatInfo(chatId: ChatId, chatInfo: ChatInfo) = httpClient.patch<Chat> {
        url(URL("$chatApiEndpoint/${chatId.id}"))
        parameter("access_token", botToken)
        contentType(ContentType.parse("application/json"))
        body = chatInfo
    }

    suspend fun sendAction(chatId: ChatId, action: Action) = httpClient.post<Default> {
        url(URL("$chatApiEndpoint/${chatId.id}/actions"))
        parameter("access_token", botToken)
        contentType(ContentType.parse("application/json"))
        body = action.name
    }

    suspend fun leaveChat(chatId: ChatId) = httpClient.delete<Default> {
        url(URL("$chatApiEndpoint/${chatId.id}/members/me"))
        parameter("access_token", botToken)
    }

    suspend fun getMembers(chatId: ChatId, count: Int = 20, marker: Long? = null) = httpClient.get<ChatMembersResult> {
        url(URL("$chatApiEndpoint/${chatId.id}/members"))
        parameter("access_token", botToken)
        parameter("count", count)
        parameter("marker", marker)
    }

    suspend fun addMembers(chatId: ChatId, userIds: List<Long>) = httpClient.post<Default> {
        url(URL("$chatApiEndpoint/${chatId.id}/members"))
        parameter("access_token", botToken)
        parameter("user_ids", userIds)
    }

    suspend fun removeMember(chatId: ChatId, userId: UserId) = httpClient.delete<Default> {
        url(URL("$chatApiEndpoint/${chatId.id}/members"))
        parameter("access_token", botToken)
        parameter("user_id", userId.id)
    }
}
