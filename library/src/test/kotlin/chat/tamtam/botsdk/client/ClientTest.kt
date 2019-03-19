package chat.tamtam.botsdk.client

import chat.tamtam.botsdk.client.retrofit.RetrofitFactory
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before

open class ClientTest {

    protected val mockServer: MockWebServer = MockWebServer()
    internal val httpManager: HttpManager = HttpManager("bot_token",
        RetrofitFactory.createRetrofit("http://localhost:8080/"))

    @Before
    fun setUp() {
        mockServer.start(8080)
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }
}