package chat.tamtam.botsdk.model.prepared

import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.MessageId
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.response.UpdateType

class UpdatesList(
    val updates: List<Update>,
    val marker: Long? = null
)

/**
 * Main interface of all updates
 *
 * @property type - this is type of updates (for example [UpdateType.CALLBACK], [UpdateType.BOT_STARTED] or etc)
 */
interface Update {
    val type: UpdateType
}

/**
 * Specific update from [Updates] with type [UpdateType.CALLBACK]
 *
 * @param timestamp - Unix-time when event has occured
 * @param message - message where exists botKeyboard [UpdateType.CALLBACK]
 * @param callback - this class you will get if user pressed on button
 */
class UpdateCallback(
    override val type: UpdateType,
    val timestamp: Long,
    val message: Message? = null,
    val callback: Callback
) : Update

/**
 * Specific update from [Updates] with type [UpdateType.MESSAGE_CREATED] or [UpdateType.MESSAGE_EDITED]
 *
 * @param timestamp - Unix-time when event has occured
 * @param message - new or edited message
 */
class UpdateMessage(
    override val type: UpdateType,
    val timestamp: Long,
    val message: Message
) : Update

/**
 * Specific update from [Updates] with type [UpdateType.MESSAGE_REMOVED]
 *
 * @param timestamp - Unix-time when event has occured
 * @param messageId - this message id you will get if user removed
 */
class UpdateMessageRemoved(
    override val type: UpdateType,
    val timestamp: Long,
    val messageId: MessageId
) : Update

/**
 * Specific update from [Updates] with type [UpdateType.BOT_STARTED], [UpdateType.BOT_ADDED] or [UpdateType.BOT_REMOVED]
 *
 * @param timestamp - Unix-time when event has occured
 * @param chatId - unique identifier of chat where bot started or added or removed
 * @param userId - user unique identifier who pressed "Start" button or added bot to chat or removed bot from chat User
 */
class UpdateBot(
    override val type: UpdateType,
    val timestamp: Long,
    val chatId: ChatId,
    val userId: UserId
) : Update

/**
 * Specific update from [Updates] with type [UpdateType.USER_ADDED]
 *
 * @param timestamp - Unix-time when event has occured
 * @param chatId - unique identifier of chat where user added
 * @param userId - user added to chat
 * @param inviterId - user who added user to chat
 */
class UpdateUserAdded(
    override val type: UpdateType,
    val timestamp: Long,
    val chatId: ChatId,
    val userId: UserId,
    val inviterId: UserId
) : Update

/**
 * Specific update from [Updates] with type [UpdateType.USER_REMOVED]
 *
 * @param timestamp - Unix-time when event has occured
 * @param chatId - unique identifier of chat where user removed
 * @param userId - see bot API docs, this parameter means different, depends on event
 * @param adminId - Administrator who removed user from chat [UpdateType.USER_REMOVED]
 */
class UpdateUserRemoved(
    override val type: UpdateType,
    val timestamp: Long,
    val chatId: ChatId,
    val userId: UserId,
    val adminId: UserId
) : Update

/**
 * Specific update from [Updates] with type [UpdateType.CHAT_TITLE_CHANGED]
 *
 * @param timestamp - Unix-time when event has occured
 * @param chatId - this chat id you will get in this event [UpdateType.BOT_STARTED], [UpdateType.CHAT_TITLE_CHANGED],
 * [UpdateType.USER_ADDED], [UpdateType.USER_REMOVED], [UpdateType.BOT_ADDED], [UpdateType.BOT_REMOVED]
 * @param userId - see bot API docs, this parameter means different, depends on event
 * @param newChatTitle - changed title in chat [UpdateType.CHAT_TITLE_CHANGED]
 */
class UpdateChatTitle(
    override val type: UpdateType,
    val timestamp: Long,
    val chatId: ChatId,
    val userId: UserId,
    val newChatTitle: String = ""
) : Update

class UpdateUnknown(
    override val type: UpdateType,
    val timestamp: Long
) : Update