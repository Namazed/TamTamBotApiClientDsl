package chat.tamtam.botsdk.communications

import chat.tamtam.botsdk.Coordinator
import chat.tamtam.botsdk.scopes.BotScope

/**
 * Main interface for all communication with Bot Api, like LongPollingCommunication or in future WebHookCommunication
 */
internal interface Communication {

    fun start(botScope: BotScope): Coordinator

}