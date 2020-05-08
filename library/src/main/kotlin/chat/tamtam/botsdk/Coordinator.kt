package chat.tamtam.botsdk

import chat.tamtam.botsdk.scopes.BotScope

interface Coordinator {

    val botScope: BotScope

    fun coordinateAsync(jsonUpdates: String)
}