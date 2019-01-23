package chat.tamtam.botsdk.scopes

import chat.tamtam.botsdk.BotMarker
import chat.tamtam.botsdk.model.response.Message

@BotMarker
class MessagesScope {

    fun answerOnMessage(answer: (callback: Message) -> Unit) {

    }
}