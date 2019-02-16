package chat.tamtam.botsdk.client

import chat.tamtam.botsdk.model.CallbackId
import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.request.AnswerCallback
import chat.tamtam.botsdk.model.request.SendMessage
import chat.tamtam.botsdk.model.response.Default
import chat.tamtam.botsdk.typing.TypingController
import io.ktor.client.features.BadResponseStatusException

class RequestsManager(
    val botHttpManager: BotHttpManager,
    val typingController: TypingController
) {

    suspend fun send(userId: UserId, sendMessage: SendMessage): ResultSend {
        return try {
            val response = botHttpManager.messageApi.sendMessage(userId, sendMessage)
            ResultSend.Success(response)
        } catch (e: BadResponseStatusException) {
            ResultSend.Failure(e)
        } catch (e: Exception) {
            ResultSend.Failure(e)
        }
    }

    suspend fun send(chatId: ChatId, sendMessage: SendMessage): ResultSend {
        return try {
            val response = botHttpManager.messageApi.sendMessage(chatId, sendMessage)
            ResultSend.Success(response)
        } catch (e: BadResponseStatusException) {
            ResultSend.Failure(e)
        } catch (e: Exception) {
            ResultSend.Failure(e)
        }
    }

    suspend fun startTyping(chatId: ChatId) {
        typingController.startTyping(chatId)
    }

    fun stopTyping(chatId: ChatId) {
        typingController.stopTyping(chatId)
    }

    suspend fun sendText(chatId: ChatId, text: String): ResultSend = send(chatId, SendMessage(text))
//
//    suspend infix fun CallbackState.send(sendMessage: SendMessage) {
//        botHttpManager.messageApi.sendMessage(UserId(callback.user.userId), sendMessage)
//    }
//
//    infix fun CallbackState.createText(text: String) : Triple<CallbackId, UserId, SendMessage> {
//        return Triple(CallbackId(callback.callbackId), UserId(callback.user.userId), SendMessage(text))
//    }
//
//    suspend infix fun Triple<CallbackId, UserId, SendMessage>.sendWith(keyboard: InlineKeyboard): ResultSend {
//        val attaches = listOf(AttachmentKeyboard(AttachType.INLINE_KEYBOARD.value.toLowerCase(), keyboard))
//        return second.send(SendMessage(third.text, attaches, third.notifyUser))
//    }
//
//    suspend infix fun CallbackState.sendText(text: String) {
//        send(SendMessage(text))
//    }

    suspend fun answer(callbackId: CallbackId, requestAnswerCallback: AnswerCallback): ResultAnswer {
        return try {
            val response = botHttpManager.answerOnCallback(callbackId, requestAnswerCallback)
            ResultAnswer.Success(response)
        } catch (e: BadResponseStatusException) {
            ResultAnswer.Failure(e)
        } catch (e: Exception) {
            ResultAnswer.Failure(e)
        }
    }

//    suspend infix fun Triple<CallbackId, UserId, SendMessage>.answerWith(keyboard: InlineKeyboard) {
//        val attaches = listOf(AttachmentKeyboard(AttachType.INLINE_KEYBOARD.value.toLowerCase(), keyboard))
//        answer(first, AnswerCallback(second.id, SendMessage(third.text, attaches)))
//    }
//
//    suspend infix fun CallbackState.answer(notification: String) {
//        answer(CallbackId(callback.callbackId), AnswerCallback(callback.user.userId, notification = notification))
//    }
//
//    infix fun CommandState.createText(text: String) : Pair<UserId, SendMessage> {
//        return Pair(UserId(command.update.message.sender.userId), SendMessage(text))
//    }
//
//    suspend infix fun Pair<UserId, SendMessage>.sendWith(keyboard: InlineKeyboard): Result {
//        val attaches = listOf(AttachmentKeyboard(AttachType.INLINE_KEYBOARD.value.toLowerCase(), keyboard))
//        return first.send(SendMessage(second.text, attaches, second.notifyUser))
//    }
}

sealed class ResultSend {
    class Success(val response: chat.tamtam.botsdk.model.response.SendMessage) : ResultSend()
    class Failure(val exception: Exception) : ResultSend()
}

sealed class ResultAnswer {
    class Success(val response: Default) : ResultAnswer()
    class Failure(val exception: Exception) : ResultAnswer()
}