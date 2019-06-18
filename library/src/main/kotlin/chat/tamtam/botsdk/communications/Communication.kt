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
     * @param startingParams - this class contains parameters for start work, look [StartingParams],
     * for different communication exists different implementation, for example [LongPollingStartingParams]
     */
    fun start(botScope: BotScope, startingParams: StartingParams): Coordinator

}