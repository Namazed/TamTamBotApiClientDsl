package chat.tamtam.botsdk.client

import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.request.Action
import chat.tamtam.botsdk.model.request.ActionWrapper
import chat.tamtam.botsdk.model.request.ChatInfo
import chat.tamtam.botsdk.model.response.Chat
import chat.tamtam.botsdk.model.response.ChatMembersResult
import chat.tamtam.botsdk.model.response.Default
import com.namazed.orthobot.botsdk.client.api.ChatApi
import com.namazed.orthobot.botsdk.client.retrofit.await
import retrofit2.HttpException
import retrofit2.Retrofit

class ChatHttpManager internal constructor(
    private val botToken: String,
    retrofit: Retrofit,
    private val chatService: ChatApi = retrofit.create(ChatApi::class.java)
) {
    suspend fun getAllChats(count: Int = 50, marker: Long? = null): List<Chat> {
        val response = chatService.getChats(botToken, count, marker).await()
        return response.body() ?: throw HttpException(response)
    }

    suspend fun getChat(chatId: ChatId): Chat {
        val response = chatService.getChat(botToken, chatId).await()
        return response.body() ?: throw HttpException(response)
    }

    suspend fun editChatInfo(chatId: ChatId, chatInfo: ChatInfo): Chat {
        val response = chatService.editChatInfo(botToken, chatId, chatInfo).await()
        return response.body() ?: throw HttpException(response)
    }

    suspend fun sendAction(chatId: ChatId, action: Action): Default {
        val response = chatService.sendAction(botToken, chatId, ActionWrapper(action.name)).await()
        return response.body() ?: throw HttpException(response)
    }

    suspend fun leaveChat(chatId: ChatId): Default {
        val response = chatService.leaveChat(botToken, chatId).await()
        return response.body() ?: throw HttpException(response)
    }

    suspend fun getMembers(chatId: ChatId, count: Int = 20, marker: Long? = null): ChatMembersResult {
        val response = chatService.getMembers(botToken, count, chatId, marker).await()
        return response.body() ?: throw HttpException(response)
    }

    suspend fun addMembers(chatId: ChatId, userIds: List<Long>): Default {
        val response = chatService.addMembers(botToken, userIds, chatId).await()
        return response.body() ?: throw HttpException(response)
    }

    suspend fun removeMember(chatId: ChatId, userId: UserId): Default {
        val response = chatService.removeMember(botToken, userId, chatId).await()
        return response.body() ?: throw HttpException(response)
    }
}
