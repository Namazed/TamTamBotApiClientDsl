package chat.tamtam.botsdk.state

import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.Command
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.response.Callback
import chat.tamtam.botsdk.model.response.Message

/**
 * Main class for all update states
 */
sealed class UpdateState

/**
 * This class created when User started bot
 * @property timestamp - unix time when started bot
 * @property chatId - chat id where User started your bot
 * @property userId - user id who started bot
 */
class StartedBotState(val timestamp: Long, val chatId: ChatId, val userId: UserId) : UpdateState()

/**
 * This class created when your bot added to chat
 * @property timestamp - unix time when added bot to chat
 * @property chatId - chat id where your bot added
 * @property userId - user id who added your bot to chat
 */
class AddedBotState(val timestamp: Long, val chatId: ChatId, val userId: UserId) : UpdateState()

/**
 * This class created when your bot removed from chat
 * @property timestamp - unix time when removed bot
 * @property chatId - chat identifier bot removed from
 * @property userId - user id who removed bot from chat
 */
class RemovedBotState(val timestamp: Long, val chatId: ChatId, val userId: UserId) : UpdateState()

/**
 * This class created when User added to chat
 * @property timestamp - unix time when added user
 * @property chatId - chat id where User added
 * @property userId - user id who added to chat
 * @property inviterId - inviter id who added user to chat
 */
class AddedUserState(val timestamp: Long, val chatId: ChatId, val userId: UserId, val inviterId: UserId) : UpdateState()

/**
 * This class created when User removed from chat
 * @property timestamp - unix time when removed user
 * @property chatId - chat id where User removed
 * @property userId - removed user id
 * @property adminId - admin id who removed user
 */
class RemovedUserState(val timestamp: Long, val chatId: ChatId, val userId: UserId, val adminId: UserId) : UpdateState()

/**
 * This class created when somebody clicked on [chat.tamtam.botsdk.model.Button] [chat.tamtam.botsdk.model.ButtonType.CALLBACK]
 * @property timestamp - unix time when somebody clicked
 * @property callback - class [Callback] which contains all needed information about click action
 */
class CallbackState(val timestamp: Long, val callback: Callback) : UpdateState()

/**
 * This class created when somebody send message for your bot in chat
 * @property timestamp - unix time when message created
 * @property message - class [Message] which contains all needed information about created message and who created it
 */
class MessageState(val timestamp: Long, val message: Message) : UpdateState()

/**
 * This class created when somebody sent message with command
 * @property timestamp - unix time when command sent
 * @property command - class [Command] which contains all needed information about command name and message which contains this command
 */
class CommandState(val timestamp: Long, val command: Command) : UpdateState()