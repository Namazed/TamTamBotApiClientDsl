package chat.tamtam.botsdk.communications

import chat.tamtam.botsdk.model.request.Subscription
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LongPollingCommunicationTest {

    @Test
    fun `check that will throw exception when create longPolling with empty BotToken`() {
        assertThrows<IllegalStateException>("Wrong startingParams, for longPolling you must use LongPollingStartingParams") {
            longPolling(LongPollingStartingParams("")) {
            }
        }
    }

    @Test
    fun `check that will throw exception when create longPolling with wrong type of params`() {
        assertThrows<IllegalStateException>("Bot token must is not empty") {
            longPolling(WebhookStartingParams("TEST", subscription = Subscription(""))) {
            }
        }
    }
}