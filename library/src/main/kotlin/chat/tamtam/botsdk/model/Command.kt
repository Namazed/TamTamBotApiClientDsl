package chat.tamtam.botsdk.model

import chat.tamtam.botsdk.UpdatesCoordinator
import chat.tamtam.botsdk.model.prepared.Message
import chat.tamtam.botsdk.model.response.ChatType

private const val SPACE = "\\s"
private val PROFILE_TAG_PATTERN = Regex("@([A-Za-z0-9_-]+)")

private val ARGUMENTS = Regex("[\\p{Punct}\\p{L}\\p{N}\\p{Sm}]+")

private val BOTS_COMMAND_PATTERN = Regex("($PROFILE_TAG_PATTERN$SPACE)?/[\\p{L}\\p{N}_]+")

private val BOTS_COMMAND_WITH_ARG_PATTERN = Regex("($PROFILE_TAG_PATTERN$SPACE)?/[\\p{L}\\p{N}_]+$SPACE?($ARGUMENTS)?")

private val BOTS_COMMAND_PATTERN_FOR_CHAT = Regex("($PROFILE_TAG_PATTERN$SPACE)/[\\p{L}\\p{N}_]+")
private val BOTS_COMMAND_WITH_ARG_PATTERN_FOR_CHAT = Regex("($PROFILE_TAG_PATTERN$SPACE)/[\\p{L}\\p{N}_]+$SPACE?($ARGUMENTS)?")

/**
 * @param name - name of command without arguments
 * @param argument - command may has some arguments after [name] with space.
 * @param timestamp - time when update created
 * @param message - this message contains in update, which handle when user created message, which contains command.
 */
data class Command(
    val name: String,
    val argument: String,
    val timestamp: Long = -1,
    val message: Message
)

/**
 * Map String with Update to Command.
 * @receiver - command name, may has some arguments
 * @param - this update handle in [UpdatesCoordinator], when user send command for bot.
 */
fun String.toCommand(message: Message, timestamp: Long) = Command(getCommandName(message.recipient.chatType),
    message.body.text.getCommandArgument(message.recipient.chatType), timestamp, message)

/**
 * This method check that String is command in Dialog.
 */
fun String.isCommand(): Boolean = this.startsWith('/') && findCommand(this, BOTS_COMMAND_WITH_ARG_PATTERN)

/**
 * This method check that String is command in Chat.
 */
fun String.isCommandInChat(): Boolean = this.startsWith('@') && findCommand(this, BOTS_COMMAND_WITH_ARG_PATTERN_FOR_CHAT)

private fun findCommand(text: String, commandRegex: Regex): Boolean {
    return commandRegex.matches(text)
}

private fun String.getCommandName(chatType: ChatType): String = if (chatType == ChatType.DIALOG) {
    toLowerCase().split(Regex(SPACE))[0]
} else {
    val splitString = toLowerCase().split(Regex(SPACE))
    "${if (splitString.isNotEmpty()) splitString[0] else ""} ${if (splitString.size > 1) splitString[1] else ""}"
}

private fun String.getCommandArgument(chatType: ChatType): String = if (chatType == ChatType.DIALOG) {
    replace(BOTS_COMMAND_PATTERN, "").trimStart()
} else {
    replace(BOTS_COMMAND_PATTERN_FOR_CHAT, "").trimStart()
}
