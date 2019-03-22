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
 * @param timestamp - unix time when started bot
 * @param chatId - chat id where User started your bot
 * @param userId - user id who started bot
 */
data class StartedBotState(val timestamp: Long, val chatId: ChatId, val userId: UserId) : UpdateState()

/**
 * This class created when your bot added to chat
 * @param timestamp - unix time when added bot to chat
 * @param chatId - chat id where your bot added
 * @param userId - user id who added your bot to chat
 */
data class AddedBotState(val timestamp: Long, val chatId: ChatId, val userId: UserId) : UpdateState()

/**
 * This class created when your bot removed from chat
 * @param timestamp - unix time when removed bot
 * @param chatId - chat identifier bot removed from
 * @param userId - user id who removed bot from chat
 */
data class RemovedBotState(val timestamp: Long, val chatId: ChatId, val userId: UserId) : UpdateState()

/**
 * This class created when User added to chat
 * @param timestamp - unix time when added user
 * @param chatId - chat id where User added
 * @param userId - user id who added to chat
 * @param inviterId - inviter id who added user to chat
 */
data class AddedUserState(val timestamp: Long, val chatId: ChatId, val userId: UserId, val inviterId: UserId) : UpdateState()

/**
 * This class created when User removed from chat
 * @param timestamp - unix time when removed user
 * @param chatId - chat id where User removed
 * @param userId - removed user id
 * @param adminId - admin id who removed user
 */
data class RemovedUserState(val timestamp: Long, val chatId: ChatId, val userId: UserId, val adminId: UserId) : UpdateState()

/**
 * This class created when somebody clicked on [chat.tamtam.botsdk.model.Button] [chat.tamtam.botsdk.model.ButtonType.CALLBACK]
 * @param timestamp - unix time when somebody clicked
 * @param callback - class [Callback] which contains all needed information about click action
 */
data class CallbackState(val timestamp: Long, val callback: Callback) : UpdateState()

/**
 * This class created when somebody send message for your bot in chat
 * @param timestamp - unix time when message created
 * @param message - class [Message] which contains all needed information about created message and who created it
 */
data class MessageState(val timestamp: Long, val message: Message) : UpdateState()

/**
 * This class created when somebody sent message with command
 * @param timestamp - unix time when command sent
 * @param command - class [Command] which contains all needed information about command name and message which contains this command
 */
data class CommandState(val timestamp: Long, val command: Command) : UpdateState()