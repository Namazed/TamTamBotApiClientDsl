package chat.tamtam.botsdk.communications

import chat.tamtam.botsdk.scopes.BotScope

/**
 * Main interface for all communication with Bot Api, like LongPollingCommunication or in future WebHookCommunication
 */
interface Communication {

    fun start(botScope: BotScope)

}