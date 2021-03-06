package chat.tamtam.botsdk.typing

import chat.tamtam.botsdk.client.ChatHttpManager
import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.request.Action
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

private const val DELAY_FOR_TYPING = 7000L

class TypingController internal constructor(
    private val httpManager: ChatHttpManager,
    private val typingScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
    private val jobs: ConcurrentHashMap<Long, Job>,
    private val log: Logger = LoggerFactory.getLogger(TypingController::class.java.name)
) {

    internal suspend fun startTyping(chatId: ChatId) {
        jobs[chatId.id] = typingScope.launch {
            while (coroutineContext.isActive) {
                httpManager.sendAction(chatId, Action.TYPING_ON)
                delay(DELAY_FOR_TYPING)
            }
        }
        log.info("startTyping: save job in map, by chatId ${chatId.id}")
    }

    internal fun stopTyping(chatId: ChatId) {
        jobs[chatId.id]?.cancel()
        val removedJob = jobs.remove(chatId.id)
        removedJob?.let { log.info("stopTyping: job removed for chatId ${chatId.id}") }
    }
}