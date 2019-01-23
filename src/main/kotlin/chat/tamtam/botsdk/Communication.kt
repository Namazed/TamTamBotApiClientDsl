package chat.tamtam.botsdk

import chat.tamtam.botsdk.scopes.BotScope

interface Communication {

    fun start(botScope: BotScope)

}