package chat.tamtam.botsdk.scopes

import chat.tamtam.botsdk.client.RequestsManager
import chat.tamtam.botsdk.state.MessageState
import chat.tamtam.botsdk.model.request.SendMessage as RequestSendMessage
import chat.tamtam.botsdk.model.response.SendMessage as ResponseSendMessage

@BotMarker
class MessagesScope(
    override val requests: RequestsManager,
    private var messagesAnswer: suspend (MessageState) -> Unit = {}
) : Scope {

    fun answerOnMessage(answer: suspend (messageState: MessageState) -> Unit) {
        messagesAnswer = answer
    }

    internal fun getAnswer() = messagesAnswer

}