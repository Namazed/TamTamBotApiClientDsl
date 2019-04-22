package chat.tamtam.botsdk.client

import chat.tamtam.botsdk.client.retrofit.Failure
import chat.tamtam.botsdk.client.retrofit.Success
import chat.tamtam.botsdk.keyboard.keyboard
import chat.tamtam.botsdk.model.AttachType
import chat.tamtam.botsdk.model.Button
import chat.tamtam.botsdk.model.ButtonType
import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.request.AttachmentKeyboard
import chat.tamtam.botsdk.model.response.ChatType
import chat.tamtam.botsdk.model.response.Messages
import kotlinx.coroutines.runBlocking
import org.junit.Test
import chat.tamtam.botsdk.model.request.SendMessage as RequestSendMessage
import chat.tamtam.botsdk.model.response.SendMessage as ResponseSendMessage

class MessageTest : ClientTest() {

    @Test
    fun `check right behavior and serialization when send message to chat`() {
        runBlocking {
            mockServer.mockHttpResponse("/json/message.json", 200)
            val result = httpManager.messageHttpManager.sendMessage(ChatId(101L), RequestSendMessage("text"))
            when (result) {
                is Success<ResponseSendMessage> -> {
                    result.response.apply {
                        assert(message.recipient.chatId == 18L)
                        assert(message.messageInfo.messageId == "test_id")
                    }
                }
                is Failure<ResponseSendMessage> -> {
                    throw AssertionError("Test finished with Success, but expected is Failure")
                }
            }
        }
    }

    @Test
    fun `check right behavior and serialization when send message to chat with keyboard`() {
        runBlocking {
            mockServer.mockHttpResponse("/json/message_with_keyboard.json", 200)
            val keyboard = keyboard {
                +buttonRow {
                    +Button(ButtonType.CALLBACK, "test_text", payload = "test_button")
                    +Button(ButtonType.CALLBACK, "test_text", payload = "test_button")
                }
            }
            val result = httpManager.messageHttpManager.sendMessage(ChatId(101L),
                RequestSendMessage("test_text", attachments = listOf(AttachmentKeyboard(AttachType.INLINE_KEYBOARD.value, keyboard))))
            when (result) {
                is Success<ResponseSendMessage> -> {
                    result.response.apply {
                        assert(message.recipient.chatId == 18L)
                        assert(message.messageInfo.messageId == "test_id")
                        assert(message.messageInfo.attachments[0].payload.buttons[0][0].title == "test_text")
                    }
                }
                else -> {}
            }
        }
    }

    @Test
    fun `check right behavior and serialization when send message with bad status`() {
        runBlocking {
            mockServer.mockHttpResponse("/json/error.json", 400)
            val result = httpManager.messageHttpManager.sendMessage(ChatId(101L), RequestSendMessage("text"))
            when (result) {
                is Success<ResponseSendMessage> -> {
                    throw AssertionError("Test finished with Success, but expected is Failure")
                }
                is Failure<ResponseSendMessage> -> {
                    result.response.apply {
                        assert(errorBody() != null)
                        assert(!isSuccessful)
                        assert(code() == 400)
                    }
                }
            }
        }
    }

    @Test
    fun `check right behavior and serialization when get all messages`() {
        runBlocking {
            mockServer.mockHttpResponse("/json/messages.json", 200)
            val result = httpManager.messageHttpManager.getAllMessages(ChatId(101L), null, 15L, 18L)
            when (result) {
                is Success<Messages> -> {
                    result.response.messages.apply {
                        assert(size == 1)
                        val message = get(0)
                        assert(message.recipient.chatType == ChatType.DIALOG)
                        assert(message.messageInfo.attachments.size == 2)
                        assert(message.messageInfo.attachments[0].latitude == 10.10)
                        assert(message.messageInfo.attachments[1].payload.photoId == 1L)
                    }
                }
                is Failure<Messages> -> {
                    throw AssertionError("Test finished with Failure, but expected is Success")
                }
            }
        }
    }

}