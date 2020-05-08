package chat.tamtam.botsdk.client

import chat.tamtam.botsdk.getJson
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

internal fun MockWebServer.mockHttpResponse(fileName: String, responseCode: Int) = this.enqueue(
    MockResponse()
        .setResponseCode(responseCode)
        .setBody(fileName.getJson())
)
