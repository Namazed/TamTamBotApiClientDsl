package chat.tamtam.botsdk.client

import chat.tamtam.botsdk.model.response.UpdateType
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import retrofit2.HttpException

class SubscriptionTest : ClientTest() {

    @Test
    fun `check right behavior and serialization when get update with callback`() = runBlocking {
        mockServer.mockHttpResponse("/json/update_callback.json", 200)
        val updates = httpManager.getUpdates(null)
        assert(updates.marker == 0L)
        assert(updates.listUpdates.size == 1)
        assert(updates.listUpdates[0].updateType == UpdateType.CALLBACK)
    }

    @Test
    fun `check right behavior and serialization when get update with 404 status`() {
        mockServer.mockHttpResponse("/json/error.json", 404)
        val updates = assertThrows<HttpException> {
            runBlocking { httpManager.getUpdates(null) }
        }
    }
}