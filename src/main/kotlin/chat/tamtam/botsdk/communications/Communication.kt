package chat.tamtam.botsdk.communications

import chat.tamtam.botsdk.scopes.BotScope

interface Communication {

    fun start(botScope: BotScope)

}