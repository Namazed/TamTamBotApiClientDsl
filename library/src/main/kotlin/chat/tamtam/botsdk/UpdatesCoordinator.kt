package chat.tamtam.botsdk

import chat.tamtam.botsdk.model.isCommand
import chat.tamtam.botsdk.model.isCommandInChat
import chat.tamtam.botsdk.model.map
import chat.tamtam.botsdk.model.prepared.Update
import chat.tamtam.botsdk.model.prepared.UpdateBot
import chat.tamtam.botsdk.model.prepared.UpdateCallback
import chat.tamtam.botsdk.model.prepared.UpdateMessage
import chat.tamtam.botsdk.model.prepared.UpdateUserAdded
import chat.tamtam.botsdk.model.prepared.UpdateUserRemoved
import chat.tamtam.botsdk.model.prepared.UpdatesList
import chat.tamtam.botsdk.model.response.UpdateType
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
        val updates: UpdatesList
        try {
            updates = withContext(parallelScope.coroutineContext) {
                botScope.botHttpManager.getUpdates(marker).map()
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

    private suspend fun coordinateUpdates(updatesList: UpdatesList) {
        if (updatesList.updates.isEmpty()) {
            return
        }
        updatesList.updates.forEachParallel { update: Update ->
            coordinate(update)
        }
    }

    private suspend fun coordinate(update: Update) {
        log.info("process: start process update with updateType ${update.type}")
        when {
            update.type == UpdateType.BOT_STARTED && update is UpdateBot -> {
                botScope.answerOnStart(StartedBotState(update.timestamp, update.chatId, update.userId))
            }
            update.type == UpdateType.BOT_ADDED && update is UpdateBot -> {
                botScope.answerOnAdd(AddedBotState(update.timestamp, update.chatId, update.userId))
            }
            update.type == UpdateType.BOT_REMOVED && update is UpdateBot -> {
                botScope.answerOnRemove(RemovedBotState(update.timestamp, update.chatId, update.userId))
            }
            update.type == UpdateType.USER_ADDED && update is UpdateUserAdded -> {
                botScope.userScope.answerOnAdd(AddedUserState(update.timestamp, update.chatId, update.userId, update.inviterId))
            }
            update.type == UpdateType.USER_REMOVED && update is UpdateUserRemoved -> {
                botScope.userScope.answerOnRemove(RemovedUserState(update.timestamp, update.chatId, update.userId, update.adminId))
            }
            update.type == UpdateType.MESSAGE_CREATED && update is UpdateMessage && (update.message.body.text.isCommand()
                    || update.message.body.text.isCommandInChat()) -> {
                val command = update.message.body.text.toCommand(update.message, update.timestamp)
                botScope.commandScope[command.name](CommandState(update.timestamp, command))
            }
            update.type == UpdateType.CALLBACK && update is UpdateCallback -> {
                val payload = update.callback.payload
                botScope.callbacksScope[payload](CallbackState(update.timestamp, update.callback.map(), update.message))
            }
            update.type == UpdateType.MESSAGE_CREATED && update is UpdateMessage -> {
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
