package chat.tamtam.botsdk.model.prepared

/**
 * This class contains info about current bot
 *
 * @param userInfo - user information for current bot (like id, name etc.)
 * @param commands - commands list of current bot, look at [Command], can be empty
 * @param description - description of current bot, can be empty
 */
data class Bot(
    val userInfo: User,
    val commands: List<Command> = emptyList(),
    val description: String = ""
)