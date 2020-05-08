package chat.tamtam.botsdk.scopes

import chat.tamtam.botsdk.client.RequestsManager
import chat.tamtam.botsdk.state.MessageState
import chat.tamtam.botsdk.model.request.SendMessage as RequestSendMessage
import chat.tamtam.botsdk.model.response.SendMessage as ResponseSendMessage

@BotMarker
class MessagesScope internal constructor(
    override val requests: RequestsManager,
    private var messagesAnswer: suspend (MessageState) -> Unit = {}
) : Scope {

    /**
     * This method save action which call when [chat.tamtam.botsdk.UpdatesCoordinator] process new message.
     *
     * @param answer - all actions in this lambda is async.
     */
    fun answerOnMessage(answer: suspend (messageState: MessageState) -> Unit) {
        messagesAnswer = answer
    }

    internal fun getAnswer() = messagesAnswer

}