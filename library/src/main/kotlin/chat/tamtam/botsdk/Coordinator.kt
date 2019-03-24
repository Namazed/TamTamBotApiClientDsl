package chat.tamtam.botsdk

import chat.tamtam.botsdk.scopes.BotScope

interface Coordinator {

    val botScope: BotScope

    suspend fun coordinateAsync(jsonUpdates: String)
}