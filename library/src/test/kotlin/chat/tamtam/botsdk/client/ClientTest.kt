package chat.tamtam.botsdk.client

import chat.tamtam.botsdk.client.retrofit.RetrofitFactory
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll

open class ClientTest {

    protected val mockServer: MockWebServer = MockWebServer()
    internal val httpManager: HttpManager = HttpManager("bot_token",
        retrofit = RetrofitFactory.createRetrofit("http://localhost:8080/", true))

    @BeforeAll
    fun setUp() {
        mockServer.start(8080)
    }

    @AfterAll
    fun tearDown() {
        mockServer.shutdown()
    }
}