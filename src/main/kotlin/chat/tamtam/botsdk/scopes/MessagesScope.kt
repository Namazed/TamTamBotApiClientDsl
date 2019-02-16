package chat.tamtam.botsdk.scopes

import chat.tamtam.botsdk.client.RequestsManager
import chat.tamtam.botsdk.state.MessageState
import chat.tamtam.botsdk.model.request.SendMessage as RequestSendMessage
import chat.tamtam.botsdk.model.response.SendMessage as ResponseSendMessage

@BotMarker
class MessagesScope(
    val requests: RequestsManager,
    private var messagesAnswer: suspend (MessageState) -> Unit = {}
) {

    fun answerOnMessage(answer: suspend (messageState: MessageState) -> Unit) {
        messagesAnswer = answer
    }

    internal fun getAnswer() = messagesAnswer

//    suspend infix fun Message.send(sendMessage: RequestSendMessage): Result {
//        var result: Result
//        try {
//            val response = botHttpManager.messageApi.sendMessage(ChatId(this.recipient.chatId), sendMessage)
//            result = Result.Success(response)
//        } catch (e: BadResponseStatusException) {
//            result = Result.Failure(e)
//        } catch (e: Exception) {
//            result = Result.Failure(e)
//        }
//        typingController.stopTyping(ChatId(this.recipient.chatId))
//        return result
//    }
//
//    suspend infix fun startTyping(chatId: ChatId) {
//        typingController.startTyping(chatId)
//    }
//
//    infix fun stopTyping(chatId: ChatId) {
//        typingController.stopTyping(chatId)
//    }
//
//    suspend infix fun Message.sendText(text: String): Result = send(RequestSendMessage(text))

}