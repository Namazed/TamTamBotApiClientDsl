package chat.tamtam.botsdk.scopes

import chat.tamtam.botsdk.client.RequestsManager
import chat.tamtam.botsdk.state.AddedUserState
import chat.tamtam.botsdk.state.RemovedUserState

class UserScope(
    override val requests: RequestsManager
) : Scope {

    internal var answerOnAdd: suspend (AddedUserState) -> Unit = {}
    internal var answerOnRemove: suspend (RemovedUserState) -> Unit = {}

    fun onAddedUserToChat(answer: suspend (AddedUserState) -> Unit) {
        answerOnAdd = answer
    }

    fun onRemovedUserFromChat(answer: suspend (RemovedUserState) -> Unit) {
        answerOnRemove = answer
    }

}
