package chat.tamtam.botsdk

import chat.tamtam.botsdk.client.HttpManager
import chat.tamtam.botsdk.model.map
import chat.tamtam.botsdk.model.prepared.UpdatesList
import chat.tamtam.botsdk.model.response.Callback
import chat.tamtam.botsdk.model.response.ChatType
import chat.tamtam.botsdk.model.response.Message
import chat.tamtam.botsdk.model.response.MessageInfo
import chat.tamtam.botsdk.model.response.Recipient
import chat.tamtam.botsdk.model.response.Update
import chat.tamtam.botsdk.model.response.UpdateType
import chat.tamtam.botsdk.model.response.Updates
import chat.tamtam.botsdk.scopes.BotScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.concurrent.atomic.AtomicBoolean

class UpdatesCoordinatorTest {

    private val botHttpManager = HttpManager("")
    private val botScope = BotScope(botHttpManager)
    private val coordinator = UpdatesCoordinator(botScope)

    @Test
    fun `check that coordinator for webhook updates throw exception when pass wrong json`() {
        assertThrows<IllegalArgumentException>("Wrong threw exception, should throw IllegalArgumentException") {
            runBlocking { coordinator.coordinateAsyncInternal("/json/answer.json".getJson()) }
        }
    }

    @Test
    fun `check that coordinator for webhook updates early return when work with empty list`() {
        var coordinateCallsCount = 0
        val stubScope = CoroutineScope(Dispatchers.Unconfined)
        val updatesCoordinator = UpdatesCoordinator(botScope, ioScope = stubScope, parallelScope = stubScope,
            delegate = object : UpdatesDelegate {
                override suspend fun coordinate(updatesList: UpdatesList) {
                    coordinateCallsCount++
                }

                override suspend fun coordinate(update: chat.tamtam.botsdk.model.prepared.Update) {
                    coordinateCallsCount++
                }

                override suspend fun coordinateParallel(updatesList: UpdatesList) {
                    coordinateCallsCount++
                }
            })
        runBlocking {
            updatesCoordinator.coordinateAsyncInternal("/json/update_empty.json".getJson())
            assert(coordinateCallsCount == 0) { "The coordinator didn't early return" }
        }
    }

    @Test
    fun `check that coordinator for webhook updates call coordinate method for one update`() {
        var coordinateCallsCount = 0
        val updatesCoordinator = UpdatesCoordinator(botScope, parallelScope = CoroutineScope(Dispatchers.Unconfined),
            delegate = object : UpdatesDelegate {
                override suspend fun coordinate(updatesList: UpdatesList) {
                }

                override suspend fun coordinate(update: chat.tamtam.botsdk.model.prepared.Update) {
                    coordinateCallsCount++
                }

                override suspend fun coordinateParallel(updatesList: UpdatesList) {
                }
            })
        runBlocking {
            updatesCoordinator.coordinateAsyncInternal("/json/update_callback.json".getJson())
            assert(coordinateCallsCount == 1) { "The coordinator didn't call right method for process update" }
        }
    }

    @Test
    fun `check that coordinator for webhook updates call coordinate method for few updates`() {
        var coordinateCallsCount = 0
        val updatesCoordinator = UpdatesCoordinator(botScope, parallelScope = CoroutineScope(Dispatchers.Unconfined),
            delegate = object : UpdatesDelegate {
                override suspend fun coordinate(updatesList: UpdatesList) {
                    coordinateCallsCount++
                }

                override suspend fun coordinate(update: chat.tamtam.botsdk.model.prepared.Update) {
                }

                override suspend fun coordinateParallel(updatesList: UpdatesList) {
                }
            })
        runBlocking {
            updatesCoordinator.coordinateAsyncInternal("/json/updates.json".getJson())
            assert(coordinateCallsCount == 1) { "The coordinator didn't call right method for process update" }
        }
    }

    @Test
    fun `check that updates coordinator work correct when processing message created`() {
        val processed = AtomicBoolean()
        botScope.messages {
            answerOnMessage {
                processed.set(true)
            }
        }
        val message = Message(timestamp = 1)
        val update = Update(1, UpdateType.MESSAGE_CREATED, message = message)
        val updates = Updates(listOf(update))
        val updatesList = updates.map()

        runBlocking {
            coordinator.coordinate(updatesList)
            assert(processed.get()) {
                "The coordinator doesn't processed message created update"
            }
        }
    }

    @Test
    fun `check that updates coordinator work correct when processing callback`() {
        val processed = AtomicBoolean()
        botScope.callbacks {
            answerOnCallback("CHUI") {
                processed.set(true)
            }
        }
        val callback = Callback(1, payload = "CHUI")
        val message = Message(timestamp = 1)
        val update = Update(1, UpdateType.CALLBACK, callback = callback, message = message)
        val updates = Updates(listOf(update))
        val updatesList = updates.map()

        runBlocking {
            coordinator.coordinate(updatesList)
            assert(processed.get()) {
                "The coordinator doesn't processed callback update"
            }
        }
    }

    @Test
    fun `check that updates coordinator work correct when processing message created which contains command`() {
        val processed = AtomicBoolean()
        botScope.commands {
            onCommand("/chui") {
                processed.set(true)
            }
        }
        val message = Message(MessageInfo(text = "/chui"), recipient = Recipient(chatType = ChatType.DIALOG), timestamp = 1)
        val update = Update(1, UpdateType.MESSAGE_CREATED, message = message)
        val updates = Updates(listOf(update))
        val updatesList = updates.map()

        runBlocking {
            coordinator.coordinate(updatesList)
            assert(processed.get()) {
                "The coordinator doesn't processed message created which contains command"
            }
        }
    }

    @Test
    fun `check that updates coordinator work correct when processing user removed`() {
        val processed = AtomicBoolean()
        botScope.users {
            onRemovedUserFromChat {
                processed.set(true)
            }
        }
        val update = Update(1, UpdateType.USER_REMOVED)
        val updates = Updates(listOf(update))
        val updatesList = updates.map()

        runBlocking {
            coordinator.coordinate(updatesList)
            assert(processed.get()) {
                "The coordinator doesn't processed user removed update"
            }
        }
    }

    @Test
    fun `check that updates coordinator work correct when processing user added`() {
        val processed = AtomicBoolean()
        botScope.users {
            onAddedUserToChat {
                processed.set(true)
            }
        }
        val update = Update(1, UpdateType.USER_ADDED)
        val updates = Updates(listOf(update))
        val updatesList = updates.map()

        runBlocking {
            coordinator.coordinate(updatesList)
            assert(processed.get()) {
                "The coordinator doesn't processed user added update"
            }
        }
    }

    @Test
    fun `check that updates coordinator work correct when processing bot removed`() {
        val processed = AtomicBoolean()
        botScope.onRemoveBotFromChat {
            processed.set(true)
        }
        val update = Update(1, UpdateType.BOT_REMOVED)
        val updates = Updates(listOf(update))
        val updatesList = updates.map()

        runBlocking {
            coordinator.coordinate(updatesList)
            assert(processed.get()) {
                "The coordinator doesn't processed bot removed update"
            }
        }
    }

    @Test
    fun `check that updates coordinator work correct when processing bot added`() {
        val processed = AtomicBoolean()
        botScope.onAddBotToChat {
            processed.set(true)
        }
        val update = Update(1, UpdateType.BOT_ADDED)
        val updates = Updates(listOf(update))
        val updatesList = updates.map()

        runBlocking {
            coordinator.coordinate(updatesList)
            assert(processed.get()) {
                "The coordinator doesn't processed bot added update"
            }
        }
    }

    @Test
    fun `check that updates coordinator work correct when processing bot started`() {
        val processed = AtomicBoolean()
        botScope.onStartBot {
            processed.set(true)
        }
        val update = Update(1, UpdateType.BOT_STARTED)
        val updates = Updates(listOf(update))
        val updatesList = updates.map()

        runBlocking {
            coordinator.coordinate(updatesList)
            assert(processed.get()) {
                "The coordinator doesn't processed bot started update"
            }
        }
    }

}