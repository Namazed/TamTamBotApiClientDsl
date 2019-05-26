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
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.SocketTimeoutException

class UpdatesHandler internal constructor(
    private val botScope: BotScope,
    private var marker: Long? = null,
    private val parallelScope: CoroutineScope = CoroutineScope(Dispatchers.Default),
    private val log: Logger = LoggerFactory.getLogger(UpdatesHandler::class.java.name)
) {

    suspend fun run(parallelWorkWithUpdates: Boolean) {
        val updates: UpdatesList
        try {
            updates = botScope.botHttpManager.getUpdates(marker).map()
            marker = updates.marker
        } catch (e: Exception) {
            if (e !is SocketTimeoutException) {
                log.error("run: error when get updates", e)
            }
            return
        }

        if (parallelWorkWithUpdates) {
            processUpdatesParallel(updates)
        } else {
            processUpdates(updates)
        }
    }

    suspend fun processUpdates(updatesList: UpdatesList) {
        if (updatesList.updates.isEmpty()) {
            return
        }
        updatesList.updates.forEachSequential { update: Update ->
            process(update)
        }
    }

    suspend fun processUpdatesParallel(updatesList: UpdatesList) {
        if (updatesList.updates.isEmpty()) {
            return
        }
        updatesList.updates.forEachParallel { update: Update ->
            process(update)
        }
    }

    private suspend fun process(update: Update) {
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

    private suspend inline fun <A> Collection<A>.forEachParallel(crossinline f: suspend (A) -> Unit) =
        map {
            log.info("forEachParallel: create async")
            parallelScope.async { f(it) }
        }.forEach {
            log.info("forEachParallel: await")
            it.await()
        }

    private suspend inline fun <A> Collection<A>.forEachSequential(crossinline f: suspend (A) -> Unit) =
        forEach {
            log.info("forEachSequence: start process")
            withContext(parallelScope.coroutineContext) { f(it) }
        }
}
