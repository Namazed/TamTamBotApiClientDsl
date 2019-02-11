package chat.tamtam.botsdk.scopes

import chat.tamtam.botsdk.client.BotHttpManager
import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.request.SendMessage
import chat.tamtam.botsdk.model.response.Message
import chat.tamtam.botsdk.typing.TypingController

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

    suspend infix fun Message.send(sendMessage: SendMessage) {
        botHttpManager.messageApi.sendMessage(ChatId(this.recipient.chatId), sendMessage)
    }

    suspend infix fun Message.sendWithTyping(sendMessage: SendMessage) {
        typingController.startTyping(ChatId(this.recipient.chatId))
        send(sendMessage)
    }

    suspend infix fun Message.sendText(text: String) {
        send(SendMessage(text))
    }

    suspend infix fun Message.sendTextWithTyping(text: String) {
        typingController.startTyping(ChatId(this.recipient.chatId))
        send(SendMessage(text))
    }
}