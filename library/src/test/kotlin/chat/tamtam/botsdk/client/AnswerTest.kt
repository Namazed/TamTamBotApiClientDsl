package chat.tamtam.botsdk.client

import chat.tamtam.botsdk.client.retrofit.Success
import chat.tamtam.botsdk.keyboard.keyboard
import chat.tamtam.botsdk.model.AttachType
import chat.tamtam.botsdk.model.Button
import chat.tamtam.botsdk.model.ButtonType
import chat.tamtam.botsdk.model.CallbackId
import chat.tamtam.botsdk.model.request.AnswerCallback
import chat.tamtam.botsdk.model.request.AttachmentKeyboard
import chat.tamtam.botsdk.model.response.Default
import kotlinx.coroutines.runBlocking
import org.junit.Test
import chat.tamtam.botsdk.model.request.SendMessage as RequestSendMessage
import chat.tamtam.botsdk.model.response.SendMessage as ResponseSendMessage

class AnswerTest : ClientTest() {

    @Test
    fun `check right behavior and serialization when answer on user callback with notification and message`() {
        runBlocking {
            mockServer.mockHttpResponse("/json/answer.json", 200)
            val result = httpManager.answerOnCallback(CallbackId("101"),
                AnswerCallback(RequestSendMessage("text"), 12L, "hello"))
            when (result) {
                is Success<Default> -> {
                    result.response.apply {
                        assert(success)
                    }
                }
                else -> {}
            }
        }
    }

    @Test
    fun `check right behavior and serialization when answer on user callback with notification and message which contains attachment`() {
        runBlocking {
            mockServer.mockHttpResponse("/json/answer.json", 200)
            val keyboard = keyboard {
                +buttonRow {
                    +Button(ButtonType.CALLBACK, "test_text", payload = "test_button")
                    +Button(ButtonType.CALLBACK, "test_text", payload = "test_button")
                }
            }
            val result = httpManager.answerOnCallback(CallbackId("101"),
                AnswerCallback(RequestSendMessage("text", attachments = listOf(AttachmentKeyboard(AttachType.INLINE_KEYBOARD.value,
                    keyboard))), 12L, "hello"))
            when (result) {
                is Success<Default> -> {
                    result.response.apply {
                        assert(success)
                    }
                }
                else -> {}
            }
        }
    }

    @Test
    fun `check right behavior and serialization when answer on user callback with notification`() {
        runBlocking {
            mockServer.mockHttpResponse("/json/answer.json", 200)
            val result = httpManager.answerOnCallback(CallbackId("101"),
                AnswerCallback(userId = 12L, notification = "hello"))
            when (result) {
                is Success<Default> -> {
                    result.response.apply {
                        assert(success)
                    }
                }
                else -> {}
            }
        }
    }

}