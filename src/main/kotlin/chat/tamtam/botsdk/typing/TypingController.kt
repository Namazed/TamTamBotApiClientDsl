package chat.tamtam.botsdk.typing

import chat.tamtam.botsdk.client.BotHttpManager
import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.request.Action
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

private const val DELAY_FOR_TYPING = 1000L

class TypingController(
    private val botHttpManager: BotHttpManager,
    private val typingScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
    private val jobs: MutableMap<Long, Job> = mutableMapOf()
) {

    suspend fun startTyping(chatId: ChatId) {
        jobs[chatId.id] = typingScope.launch {
            while (isActive) {
                val default = botHttpManager.chatApi.sendAction(chatId, Action.TYPING_ON)
                if (default.success) {
                    delay(DELAY_FOR_TYPING)
                }
            }
        }
    }

    fun stopTyping(chatId: ChatId) {
        jobs[chatId.id]?.cancel()
        jobs.remove(chatId.id)
    }
}