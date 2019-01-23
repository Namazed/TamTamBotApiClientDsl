package chat.tamtam.botsdk

import chat.tamtam.botsdk.model.response.Update
import chat.tamtam.botsdk.model.response.Updates
import chat.tamtam.botsdk.scopes.BotScope
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class UpdateParsing(
    private val botScope: BotScope,
    private val log: Logger = LoggerFactory.getLogger(UpdateParsing::class.java.name)
) {

    suspend fun run() {
        val updates: Updates
        try {
            updates = botScope.botHttpManager.getUpdates()
        } catch (e: Exception) {
            log.error("run: error when get updates", e)
            return
        }

        updatesProcessing(updates)
    }

    private fun updatesProcessing(updates: Updates) {
        updates.listUpdates.forEach { update: Update ->
            when {

            }
        }
    }
}