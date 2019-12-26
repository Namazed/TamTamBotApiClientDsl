package chat.tamtam.botsdk.communications

import chat.tamtam.botsdk.assertThrow
import chat.tamtam.botsdk.model.request.Subscription
import org.junit.jupiter.api.Test

class LongPollingCommunicationTest {

    @Test
    fun `check that will throw exception when create longPolling with empty BotToken`() {
        assertThrow {
            longPolling(LongPollingStartingParams("")) {
            }
        }
    }

    @Test
    fun `check that will throw exception when create longPolling with wrong type of params`() {
        assertThrow {
            longPolling(WebhookStartingParams("TEST", subscription = Subscription(""))) {
            }
        }
    }
}