package chat.tamtam.botsdk.model

import chat.tamtam.botsdk.model.response.EMPTY_MESSAGE
import chat.tamtam.botsdk.model.response.Message
import chat.tamtam.botsdk.model.response.Update

private const val SPACE = "\\s"
private val PROFILE_TAG_PATTERN = Regex("@([A-Za-z0-9_-]+)")

private val ARGUMENTS = Regex("[\\p{Punct}\\p{L}\\p{N}\\p{Sm}]+")

private val BOTS_COMMAND_PATTERN = Regex("($PROFILE_TAG_PATTERN$SPACE)?/[\\p{L}\\p{N}_]+")

private val BOTS_COMMAND_DESCRIPTION_PATTERN = Regex("($PROFILE_TAG_PATTERN$SPACE)?/[\\p{L}\\p{N}_]+$SPACE($ARGUMENTS)?")

private val BOTS_COMMAND_PATTERN_FOR_CHAT = Regex("($PROFILE_TAG_PATTERN$SPACE)/[\\p{L}\\p{N}_]+")
private val BOTS_COMMAND_DESCRIPTION_PATTERN_FOR_CHAT = Regex("($PROFILE_TAG_PATTERN$SPACE)/[\\p{L}\\p{N}_]+$SPACE($ARGUMENTS)?")

class Command(
    val name: String,
    val timestamp: Long = -1,
    val message: Message = EMPTY_MESSAGE
)

fun String.toCommand(update: Update) = Command(this, update.timestamp, update.message)

fun String.isCommand(): Boolean = this.startsWith('/') && findCommand(this, BOTS_COMMAND_PATTERN)
fun String.isCommandInChat(): Boolean = this.startsWith('@') && findCommand(this, BOTS_COMMAND_PATTERN_FOR_CHAT)

private fun findCommand(text: String, commandRegex: Regex): Boolean {
    commandRegex.findAll(text).map { commandResult: MatchResult ->
        if (commandResult.groups.size == 1) {
            return@map true
        }
        return@map false

    }

    return false
}