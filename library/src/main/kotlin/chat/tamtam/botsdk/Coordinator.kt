package chat.tamtam.botsdk

import chat.tamtam.botsdk.model.response.Updates
import chat.tamtam.botsdk.scopes.BotScope

interface Coordinator {

    val botScope: BotScope

    suspend fun coordinateAsync(updates: Updates)
}