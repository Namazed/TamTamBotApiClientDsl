package chat.tamtam.botsdk

import chat.tamtam.botsdk.client.ClientTest
import org.junit.jupiter.api.assertThrows
import java.io.File

internal fun String.getJson(): String {
    val uri = ClientTest::class.java.getResource(this)
    return String(File(uri.file).readBytes())
}

internal inline fun assertThrow(crossinline executable: () -> Unit) =
    assertThrows<IllegalStateException>("Wrong threw exception, should throw IllegalStateException") {
        executable()
    }