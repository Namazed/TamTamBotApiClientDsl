package chat.tamtam.botsdk

import chat.tamtam.botsdk.model.map
import chat.tamtam.botsdk.model.prepared.Update
import chat.tamtam.botsdk.model.prepared.UpdatesList
import chat.tamtam.botsdk.model.response.Updates
import chat.tamtam.botsdk.scopes.BotScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.SocketTimeoutException

class UpdatesCoordinator internal constructor(
    override val botScope: BotScope,
    private var marker: Long? = null,
    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO),
    private val parallelScope: CoroutineScope = CoroutineScope(Dispatchers.Default),
    private val log: Logger = LoggerFactory.getLogger(UpdatesCoordinator::class.java.name),
    private val delegate: UpdatesDelegate = DefaultUpdatesDelegate(botScope, parallelScope, log)
): Coordinator {

    @UseExperimental(UnstableDefault::class)
    override fun coordinateAsync(jsonUpdates: String) {
        ioScope.launch {
            coordinateAsyncInternal(jsonUpdates)
        }
    }

    @UseExperimental(UnstableDefault::class)
    internal suspend fun coordinateAsyncInternal(jsonUpdates: String) {
        val updates: Updates = try {
            Json.nonstrict.parse(Updates.serializer(), jsonUpdates)
        } catch (e: Exception) {
            throw IllegalArgumentException("Wrong json, you need pass json with Updates class", e)
        }

        if (updates.listUpdates.isEmpty()) {
            return
        }

        val updatesList = updates.map()

        if (updatesList.updates.size > 1) {
            delegate.coordinate(updatesList)
            return
        }

        withContext(parallelScope.coroutineContext) {
            delegate.coordinate(updatesList.updates[0])
        }
    }

    internal suspend fun run(parallelWorkWithUpdates: Boolean) {
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

        if (parallelWorkWithUpdates) {
            delegate.coordinateParallel(updates)
        } else {
            delegate.coordinate(updates)
        }
    }

    internal suspend fun coordinate(updatesList: UpdatesList) {
        delegate.coordinate(updatesList)
    }

    class DefaultUpdatesDelegate(
        private val botScope: BotScope,
        private val parallelScope: CoroutineScope = CoroutineScope(Dispatchers.Default),
        private val log: Logger = LoggerFactory.getLogger(UpdatesCoordinator::class.java.name)
    ) : UpdatesDelegate {

        override suspend fun coordinate(updatesList: UpdatesList) {
            if (updatesList.updates.isEmpty()) {
                return
            }
            updatesList.updates.forEachSequential { update: Update ->
                coordinate(update)
            }
        }

        override suspend fun coordinateParallel(updatesList: UpdatesList) {
            if (updatesList.updates.isEmpty()) {
                return
            }
            updatesList.updates.forEachParallel { update: Update ->
                coordinate(update)
            }
        }

        override suspend fun coordinate(update: Update) {
            log.info("process: start process update with updateType ${update.type}")
            update.process(botScope, log)
        }

        private suspend inline fun <A> Collection<A>.forEachParallel(crossinline f: suspend (A) -> Unit) =
            map {
                log.info("forEachParallel: create async")
                parallelScope.async { f(it) }
            }.forEach {
                log.info("forEachParallel: await")
                it.await()
            }

        private suspend inline fun <A> Collection<A>.mapParallel(crossinline f: suspend (A) -> Unit) =
            map {
                log.info("forEachParallel: create async")
                parallelScope.async { f(it) }
            }.toList().awaitAll()

        private suspend inline fun <A> Collection<A>.forEachSequential(crossinline f: suspend (A) -> Unit) =
            forEach {
                log.info("forEachSequence: start process")
                withContext(parallelScope.coroutineContext) { f(it) }
            }

    }
}
