package chat.tamtam.botsdk.communications

import chat.tamtam.botsdk.client.ClientTest
import chat.tamtam.botsdk.model.request.Subscription
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.slf4j.LoggerFactory

class WebhookCommunicationTest : ClientTest() {

    @Test
    fun `check that will throw exception when create webhook with wrong type of params`() {
        assertThrows<IllegalStateException>("Wrong startingParams, for webhook you must use WebhookStartingParams") {
            webhook(LongPollingStartingParams("")) {
            }
        }
    }

    @Test
    fun `check that will throw exception when create webhook with empty BotToken`() {
        assertThrows<IllegalStateException>("Bot token must is not empty") {
            webhook(WebhookStartingParams("", subscription = Subscription(""))) {
            }
        }
    }

    @Test
    fun `check that will throw exception when create webhook with empty Subscription Url`() {
        assertThrows<IllegalStateException>("Your url must is not empty") {
            webhook(WebhookStartingParams("TEST", subscription = Subscription(""))) {
            }
        }
    }

    @Test
    fun `check that webhook will be created right`() {
        val botToken = "TEST"
        val webhookCommunication = WebhookCommunication(botToken, CoroutineScope(Dispatchers.Unconfined))
        val coordinator = webhookCommunication.init(
            httpManager, WebhookStartingParams(botToken, subscription = Subscription("TEST_URL")),
            LoggerFactory.getLogger(WebhookCommunication::class.java.name), {})

        val request = mockServer.takeRequest()
        assert(coordinator.botScope.botHttpManager === httpManager) {
            "Wrong httpManager in BotScope"
        }
        assert(mockServer.requestCount == 1) {
            "Wrong count :${mockServer.requestCount}: of server requests when starting webhook"
        }
        assert(request.method == "POST") {
            "Wrong request method :${request.method}:"
        }
        assert(request.path == "/subscriptions?access_token=bot_token") {
            "Wrong request path :${request.path}:"
        }
    }
}