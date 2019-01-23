package chat.tamtam.botsdk.scopes

import chat.tamtam.botsdk.BotMarker
import chat.tamtam.botsdk.model.Payload
import chat.tamtam.botsdk.model.response.Callback

@BotMarker
class CallbacksScope {

    fun defaultAnswer(defaultAnswer: (callback: Callback) -> Unit) {

    }

    fun answerOnCallback(payloadButton: Payload, answer: (callback: Callback) -> Unit) {

    }
}