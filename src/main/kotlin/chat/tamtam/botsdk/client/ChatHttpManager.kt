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

    suspend fun getChat(chatId: ChatId): HttpResult<Chat> = chatService.getChat(chatId, botToken).await()

    suspend fun editChatInfo(chatId: ChatId, chatInfo: ChatInfo): HttpResult<Chat> =
        chatService.editChatInfo(chatId, botToken, chatInfo).await()

    suspend fun sendAction(chatId: ChatId, action: Action): HttpResult<Default> =
        chatService.sendAction(chatId, botToken, ActionWrapper(action.value)).await()

    suspend fun leaveChat(chatId: ChatId): HttpResult<Default> = chatService.leaveChat(chatId, botToken).await()

    suspend fun getMembers(chatId: ChatId, count: Int = 20, marker: Long? = null): HttpResult<ChatMembersResult> =
        chatService.getMembers(chatId, botToken, count, marker).await()

    suspend fun addMembers(chatId: ChatId, userIds: List<Long>): HttpResult<Default> =
        chatService.addMembers(chatId, botToken, userIds).await()

    suspend fun removeMember(chatId: ChatId, userId: UserId): HttpResult<Default> =
        chatService.removeMember(chatId, botToken, userId).await()
}
