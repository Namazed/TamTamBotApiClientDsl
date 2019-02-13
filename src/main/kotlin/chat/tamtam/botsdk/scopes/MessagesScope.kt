package chat.tamtam.botsdk.scopes

import chat.tamtam.botsdk.client.BotHttpManager
import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.response.Message
import chat.tamtam.botsdk.typing.TypingController
import io.ktor.client.features.BadResponseStatusException
import chat.tamtam.botsdk.model.request.SendMessage as RequestSendMessage
import chat.tamtam.botsdk.model.response.SendMessage as ResponseSendMessage

@BotMarker
class MessagesScope(
    private val botHttpManager: BotHttpManager,
    private var messagesAnswer: suspend (Message) -> Unit = {},
    private val typingController: TypingController = TypingController(botHttpManager)
) {

    fun answerOnMessage(answer: suspend (message: Message) -> Unit) {
        messagesAnswer = answer
    }

    internal fun getAnswer() = messagesAnswer

    suspend infix fun Message.send(sendMessage: RequestSendMessage): Result {
        var result: Result
        try {
            val response = botHttpManager.messageApi.sendMessage(ChatId(this.recipient.chatId), sendMessage)
            result = Result.Success(response)
        } catch (e: BadResponseStatusException) {
            result = Result.Failure(e)
        } catch (e: Exception) {
            result = Result.Failure(e)
        }
        typingController.stopTyping(ChatId(this.recipient.chatId))
        return result
    }

    suspend infix fun startTyping(chatId: ChatId) {
        typingController.startTyping(chatId)
    }

    infix fun stopTyping(chatId: ChatId) {
        typingController.stopTyping(chatId)
    }

    suspend infix fun Message.sendText(text: String): Result = send(RequestSendMessage(text))

}

sealed class Result {
    class Success(val response: ResponseSendMessage) : Result()
    class Failure(val exception: Exception) : Result()
}