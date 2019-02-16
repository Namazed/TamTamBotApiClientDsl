package chat.tamtam.botsdk.scopes

import chat.tamtam.botsdk.client.BotHttpManager
import chat.tamtam.botsdk.state.AddedUserState
import chat.tamtam.botsdk.state.RemovedUserState
import chat.tamtam.botsdk.typing.TypingController

class UserScope(
    private val botHttpManager: BotHttpManager,
    private val typingController: TypingController = TypingController(botHttpManager)
) {

    internal var answerOnAdd: suspend (AddedUserState) -> Unit = {}
    internal var answerOnRemove: suspend (RemovedUserState) -> Unit = {}

    fun onAddedUserToChat(answer: suspend (AddedUserState) -> Unit) {
        answerOnAdd = answer
    }

    fun onRemovedUserFromChat(answer: suspend (RemovedUserState) -> Unit) {
        answerOnRemove = answer
    }

//    suspend infix fun Message.send(sendMessage: SendMessage): Result {
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
//    suspend infix fun Message.sendText(text: String): Result = send(SendMessage(text))

}
