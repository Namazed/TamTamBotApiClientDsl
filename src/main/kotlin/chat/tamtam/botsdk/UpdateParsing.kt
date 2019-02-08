package chat.tamtam.botsdk

import chat.tamtam.botsdk.model.Payload
import chat.tamtam.botsdk.model.isCommand
import chat.tamtam.botsdk.model.response.Update
import chat.tamtam.botsdk.model.response.Updates
import chat.tamtam.botsdk.model.response.isNotEmptyCallback
import chat.tamtam.botsdk.model.response.isNotEmptyMessage
import chat.tamtam.botsdk.model.toCommand
import chat.tamtam.botsdk.scopes.BotScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors

class UpdateParsing(
    private val botScope: BotScope,
    private val context: ExecutorCoroutineDispatcher = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()).asCoroutineDispatcher(),
    private val parallelScope: CoroutineScope = CoroutineScope(context),
    private val log: Logger = LoggerFactory.getLogger(UpdateParsing::class.java.name)
) {

    suspend fun run() {
        val updates: Updates
        try {
            updates = botScope.botHttpManager.getUpdates()
        } catch (e: Exception) {
            log.error("run: error when get updates", e)
            return
        }

        updatesProcessing(updates)
    }

    private suspend fun updatesProcessing(updates: Updates) {
        updates.listUpdates.forEachParallel { update: Update ->
            process(update)
        }
    }

    private suspend fun process(update: Update) {
        when {
            isNotEmptyMessage(update.message) && update.message.messageInfo.text.isCommand() -> {
                val command = update.message.messageInfo.text.toCommand(update)
                botScope.commandScope[command.name](command)
            }
            isNotEmptyMessage(update.message) -> {
                botScope.messagesScope.getAnswer()(update.message)
            }
            isNotEmptyCallback(update.callback) -> {
                val callback = update.callback
                botScope.callbacksScope[Payload(callback.payload)](callback)
            }
        }
    }

    private suspend fun <A> Collection<A>.forEachParallel(f: suspend (A) -> Unit): Unit =
        map { parallelScope.async { f(it) } }.forEach { it.await() }
}