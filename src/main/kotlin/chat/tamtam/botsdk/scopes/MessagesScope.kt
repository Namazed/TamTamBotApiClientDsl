package chat.tamtam.botsdk.scopes

import chat.tamtam.botsdk.client.BotHttpManager
import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.request.SendMessage
import chat.tamtam.botsdk.model.response.Message

@BotMarker
class MessagesScope(
    private val botHttpManager: BotHttpManager,
    private var messagesAnswer: suspend (Message) -> Unit = {}
) {

    fun answerOnMessage(answer: suspend (message: Message) -> Unit) {
        messagesAnswer = answer
    }

    internal fun getAnswer() = messagesAnswer

    suspend infix fun Message.send(sendMessage: SendMessage) {
        when (this.recipient.chatId) {
            -1L -> {
                botHttpManager.sendMessage(UserId(this.recipient.chatId), sendMessage)
            }
            else -> {
                botHttpManager.sendMessage(ChatId(this.recipient.chatId), sendMessage)
            }
        }
    }

    suspend infix fun Message.sendText(text: String) {
        send(SendMessage(text))
    }
}