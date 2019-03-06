package chat.tamtam.botsdk.client

import chat.tamtam.botsdk.client.retrofit.HttpResult
import chat.tamtam.botsdk.client.retrofit.await
import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.request.Action
import chat.tamtam.botsdk.model.request.ActionWrapper
import chat.tamtam.botsdk.model.request.ChatInfo
import chat.tamtam.botsdk.model.response.Chat
import chat.tamtam.botsdk.model.response.ChatMembersResult
import chat.tamtam.botsdk.model.response.Default
import com.namazed.orthobot.botsdk.client.api.ChatApi
import retrofit2.Retrofit

class ChatHttpManager internal constructor(
    private val botToken: String,
    retrofit: Retrofit,
    private val chatService: ChatApi = retrofit.create(ChatApi::class.java)
) {
    suspend fun getAllChats(count: Int = 50, marker: Long? = null): HttpResult<List<Chat>> =
        chatService.getChats(botToken, count, marker).await()

    suspend fun getChat(chatId: ChatId): HttpResult<Chat> = chatService.getChat(botToken, chatId).await()

    suspend fun editChatInfo(chatId: ChatId, chatInfo: ChatInfo): HttpResult<Chat> =
        chatService.editChatInfo(botToken, chatId, chatInfo).await()

    suspend fun sendAction(chatId: ChatId, action: Action): HttpResult<Default> =
        chatService.sendAction(botToken, chatId, ActionWrapper(action.name)).await()

    suspend fun leaveChat(chatId: ChatId): HttpResult<Default> = chatService.leaveChat(botToken, chatId).await()

    suspend fun getMembers(chatId: ChatId, count: Int = 20, marker: Long? = null): HttpResult<ChatMembersResult> =
        chatService.getMembers(botToken, count, chatId, marker).await()

    suspend fun addMembers(chatId: ChatId, userIds: List<Long>): HttpResult<Default> =
        chatService.addMembers(botToken, userIds, chatId).await()

    suspend fun removeMember(chatId: ChatId, userId: UserId): HttpResult<Default> =
        chatService.removeMember(botToken, userId, chatId).await()
}
