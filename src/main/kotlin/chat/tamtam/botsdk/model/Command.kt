package chat.tamtam.botsdk.model

import chat.tamtam.botsdk.model.response.Update

val BOTS_COMMAND_PATTERN = Regex("/[\\p{L}\\p{N}_]+(@([A-Za-z0-9_-]+))?")

class Command(
    val name: String,
    val update: Update = Update(-1, "")
)

fun String.toCommand(update: Update) = Command(this, update)

fun String.isCommand(): Boolean = this.startsWith('/') && findCommand(this, BOTS_COMMAND_PATTERN)

private fun findCommand(text: String, commandRegex: Regex): Boolean {
    commandRegex.findAll(text).map {commandResult: MatchResult ->
        if (commandResult.groups.size == 1) {
            return@map true
        }
        return@map false

    }

    return false
}