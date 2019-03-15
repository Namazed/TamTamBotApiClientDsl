package chat.tamtam.botsdk.scopes

import chat.tamtam.botsdk.client.RequestsManager
import chat.tamtam.botsdk.state.AddedUserState
import chat.tamtam.botsdk.state.RemovedUserState

class UserScope internal constructor(
    override val requests: RequestsManager
) : Scope {

    internal var answerOnAdd: suspend (AddedUserState) -> Unit = {}
    internal var answerOnRemove: suspend (RemovedUserState) -> Unit = {}

    /**
     * This method save action which call when [chat.tamtam.botsdk.UpdatesHandler] process new update
     * with type [chat.tamtam.botsdk.model.response.UpdateType.USER_ADDED].
     *
     * @param answer - all actions in this lambda is async.
     */
    fun onAddedUserToChat(answer: suspend (AddedUserState) -> Unit) {
        answerOnAdd = answer
    }

    /**
     * This method save action which call when [chat.tamtam.botsdk.UpdatesHandler] process new update
     * with type [chat.tamtam.botsdk.model.response.UpdateType.USER_REMOVED].
     *
     * @param answer - all actions in this lambda is async.
     */
    fun onRemovedUserFromChat(answer: suspend (RemovedUserState) -> Unit) {
        answerOnRemove = answer
    }

}
