package chat.tamtam.botsdk.communications

import chat.tamtam.botsdk.Coordinator
import chat.tamtam.botsdk.scopes.BotScope

/**
 * Main interface for all communication with Bot Api, like LongPollingCommunication or in future WebHookCommunication
 */
internal interface Communication {

    /**
     * This method start communication, for example longPolling or webhook.
     *
     * @param botScope - main scope, which contains all other scopes, RequestsManager and TypingController
     * @param async - this flag mean that longPolling communication start polling on another single thread
     */
    fun start(botScope: BotScope, async: Boolean): Coordinator

}