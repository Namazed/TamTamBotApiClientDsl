package chat.tamtam.botsdk.typing

import chat.tamtam.botsdk.client.BotHttpManager
import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.request.Action
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class TypingController(
    private val botHttpManager: BotHttpManager,
    private val typingScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
) {

    suspend fun startTyping(chatId: ChatId) {
        botHttpManager.sendAction(chatId, Action.TYPING_ON)
    }

    suspend fun stopTyping(chatId: ChatId) {
        botHttpManager.sendAction(chatId, Action.TYPING_OFF)
    }
}