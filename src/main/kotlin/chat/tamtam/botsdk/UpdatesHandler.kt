package chat.tamtam.botsdk

import chat.tamtam.botsdk.model.*
import chat.tamtam.botsdk.model.Payload
import chat.tamtam.botsdk.model.response.*
import chat.tamtam.botsdk.scopes.BotScope
import chat.tamtam.botsdk.state.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors

class UpdatesHandler(
    private val botScope: BotScope,
    private val context: ExecutorCoroutineDispatcher = Executors.newFixedThreadPool(getAvailableThread()).asCoroutineDispatcher(),
    private val parallelScope: CoroutineScope = CoroutineScope(context),
    private val log: Logger = LoggerFactory.getLogger(UpdatesHandler::class.java.name)
) {

    suspend fun run() {
        val updates: Updates
        try {
            updates = botScope.botHttpManager.getUpdates()
        } catch (e: Exception) {
            log.error("run: error when get updates", e)
            return
        }

        processUpdates(updates)
    }

    private suspend fun processUpdates(updates: Updates) {
        updates.listUpdates.forEachParallel { update: Update ->
            process(update)
        }
    }

    private suspend fun process(update: Update) {
        when {
            update.updateType == UpdateType.BOT_STARTED -> {
                botScope.answerOnStart(StartedBotState(update.timestamp, ChatId(update.chatId), UserId(update.userId)))
            }
            update.updateType == UpdateType.BOT_ADDED -> {
                botScope.answerOnAdd(AddedBotState(update.timestamp, ChatId(update.chatId), UserId(update.userId)))
            }
            update.updateType == UpdateType.BOT_REMOVED -> {
                botScope.answerOnRemove(RemovedBotState(update.timestamp, ChatId(update.chatId), UserId(update.userId)))
            }
            update.updateType == UpdateType.USER_ADDED -> {
                botScope.userScope.answerOnAdd(AddedUserState(update.timestamp, ChatId(update.chatId),
                    UserId(update.userId), UserId(update.inviterId)
                ))
            }
            update.updateType == UpdateType.USER_REMOVED -> {
                botScope.userScope.answerOnRemove(RemovedUserState(update.timestamp, ChatId(update.chatId),
                    UserId(update.userId), UserId(update.adminId)
                ))
            }
            isNotEmptyMessage(update.message) && update.message.messageInfo.text.isCommand() -> {
                val command = update.message.messageInfo.text.toCommand(update)
                botScope.commandScope[command.name](CommandState(update.timestamp, command))
            }
            isNotEmptyMessage(update.message) -> {
                botScope.messagesScope.getAnswer()(MessageState(update.timestamp, update.message))
            }
            isNotEmptyCallback(update.callback) -> {
                val callback = update.callback
                botScope.callbacksScope[Payload(callback.payload)](CallbackState(update.timestamp, update.callback))
            }
        }
    }

    private suspend fun <A> Collection<A>.forEachParallel(f: suspend (A) -> Unit): Unit =
        map { parallelScope.async { f(it) } }.forEach { it.await() }
}

private fun getAvailableThread(): Int {
    val availableProcessors = Runtime.getRuntime().availableProcessors()
    return when {
        availableProcessors > 2 -> availableProcessors - 1
        else -> availableProcessors
    }
}