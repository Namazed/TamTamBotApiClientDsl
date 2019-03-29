package chat.tamtam.botsdk

import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.Payload
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.isCommand
import chat.tamtam.botsdk.model.isCommandInChat
import chat.tamtam.botsdk.model.response.EMPTY_MESSAGE
import chat.tamtam.botsdk.model.response.Update
import chat.tamtam.botsdk.model.response.UpdateType
import chat.tamtam.botsdk.model.response.Updates
import chat.tamtam.botsdk.model.response.isNotEmptyCallback
import chat.tamtam.botsdk.model.response.isNotEmptyMessage
import chat.tamtam.botsdk.model.toCommand
import chat.tamtam.botsdk.scopes.BotScope
import chat.tamtam.botsdk.state.AddedBotState
import chat.tamtam.botsdk.state.AddedUserState
import chat.tamtam.botsdk.state.CallbackState
import chat.tamtam.botsdk.state.CommandState
import chat.tamtam.botsdk.state.MessageState
import chat.tamtam.botsdk.state.RemovedBotState
import chat.tamtam.botsdk.state.RemovedUserState
import chat.tamtam.botsdk.state.StartedBotState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.SocketTimeoutException

class UpdatesCoordinator internal constructor(
    override val botScope: BotScope,
    private var marker: Long? = null,
    private val parallelScope: CoroutineScope = CoroutineScope(Dispatchers.Default),
    private val log: Logger = LoggerFactory.getLogger(UpdatesCoordinator::class.java.name)
): Coordinator {

    override suspend fun coordinateAsync(jsonUpdates: String) {
        val updates: Updates = try {
            Json.parse(Updates.serializer(), jsonUpdates)
        } catch (e: Exception) {
            throw IllegalArgumentException("Wrong json, you need pass json with Updates class", e)
        }

        if (updates.listUpdates.isEmpty()) {
            return
        }

        if (updates.listUpdates.size > 1) {
            coordinateUpdates(updates)
            return
        }

        withContext(parallelScope.coroutineContext) {
            coordinate(updates.listUpdates[0])
        }
    }

    internal suspend fun run() {
        val updates: Updates
        try {
            updates = withContext(parallelScope.coroutineContext) {
                botScope.botHttpManager.getUpdates(marker)
            }
            marker = updates.marker
        } catch (e: Exception) {
            if (e !is SocketTimeoutException) {
                log.error("run: error when get updates", e)
            }
            return
        }

        coordinateUpdates(updates)
    }

    private suspend fun coordinateUpdates(updates: Updates) {
        if (updates.listUpdates.isEmpty()) {
            return
        }
        updates.listUpdates.forEachParallel { update: Update ->
            coordinate(update)
        }
    }

    private suspend fun coordinate(update: Update) {
        log.info("process: start process update with updateType ${update.updateType}")
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
            isNotEmptyMessage(update.message) && (update.message.messageInfo.text.isCommand()
                    || update.message.messageInfo.text.isCommandInChat()) -> {
                val command = update.message.messageInfo.text.toCommand(update)
                botScope.commandScope[command.name](CommandState(update.timestamp, command))
            }
            isNotEmptyCallback(update.callback) -> {
                val callback = update.callback
                val message = if (update.message == EMPTY_MESSAGE) null else update.message
                botScope.callbacksScope[Payload(callback.payload)](CallbackState(update.timestamp, callback, message))
            }
            isNotEmptyMessage(update.message) -> {
                botScope.messagesScope.getAnswer()(MessageState(update.timestamp, update.message))
            }
        }
    }

    private suspend fun <A> Collection<A>.forEachParallel(f: suspend (A) -> Unit): Unit =
        map {
            log.info("forEachParallel: create async")
            parallelScope.async { f(it) }
        }.forEach {
            log.info("forEachParallel: await")
            it.await()
        }
}
