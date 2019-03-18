package chat.tamtam.botsdk

import chat.tamtam.botsdk.client.ClientTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import java.io.File

internal fun MockWebServer.mockHttpResponse(fileName: String, responseCode: Int) = this.enqueue(
    MockResponse()
        .setResponseCode(responseCode)
        .setBody(fileName.getJson())
)

private fun String.getJson(): String {
    val uri = ClientTest::class.java.getResource(this)
    return String(File(uri.file).readBytes())
}
