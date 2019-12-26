package chat.tamtam.botsdk.model.prepared

import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.MessageId
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.isCommand
import chat.tamtam.botsdk.model.isCommandInChat
import chat.tamtam.botsdk.model.map
import chat.tamtam.botsdk.model.response.UpdateType
import chat.tamtam.botsdk.model.toCommand
import chat.tamtam.botsdk.scopes.BotScope
import chat.tamtam.botsdk.state.AddedBotState
import chat.tamtam.botsdk.state.AddedUserState
import chat.tamtam.botsdk.state.CallbackState
import chat.tamtam.botsdk.state.CommandState
import chat.tamtam.botsdk.state.MessageState
import chat.tamtam.botsdk.state.RemovedBotState
import chat.tamtam.botsdk.state.RemovedUserState
import chat.tamtam.botsdk.state.StartedBotState
import org.slf4j.Logger

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

    suspend fun process(botScope: BotScope, logger: Logger)
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
) : Update {
    override suspend fun process(botScope: BotScope, logger: Logger) {
        val payload = callback.payload
        botScope.callbacksScope[payload](CallbackState(timestamp, callback.map(), message))
    }
}

/**
 * Specific update from [UpdatesList] with type [UpdateType.MESSAGE_CREATED] or [UpdateType.MESSAGE_EDITED]
 *
 * @param timestamp - Unix-time when event has occured
 * @param message - new or edited message
 */
class UpdateMessage(
    override val type: UpdateType,
    val timestamp: Long,
    val message: Message
) : Update {
    override suspend fun process(botScope: BotScope, logger: Logger) = when {
        message.body.text.isCommand() || message.body.text.isCommandInChat() -> {
            val command = message.body.text.toCommand(message, timestamp)
            botScope.commandScope[command.name](CommandState(timestamp, command))
        }
        else -> {
            botScope.messagesScope.getAnswer()(MessageState(timestamp, message))
        }
    }

}

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
) : Update {
    override suspend fun process(botScope: BotScope, logger: Logger) {
        logger.warn("Processing for Update with type $type didn't implement")
    }
}

/**
 * Specific update from [Updates] with type [UpdateType.BOT_STARTED], [UpdateType.BOT_ADDED] or [UpdateType.BOT_REMOVED]
 *
 * @param timestamp - Unix-time when event has occured
 * @param chatId - unique identifier of chat where bot started or added or removed
 * @param user - user who pressed "Start" button or added bot to chat or removed bot from chat
 */
class UpdateBot(
    override val type: UpdateType,
    val timestamp: Long,
    val chatId: ChatId,
    val user: User
) : Update {
    override suspend fun process(botScope: BotScope, logger: Logger) = when (type) {
        UpdateType.BOT_STARTED -> {
            botScope.answerOnStart(StartedBotState(timestamp, chatId, user))
        }
        UpdateType.BOT_ADDED -> {
            botScope.answerOnAdd(AddedBotState(timestamp, chatId, user))
        }
        UpdateType.BOT_REMOVED -> {
            botScope.answerOnRemove(RemovedBotState(timestamp, chatId, user))
        }
        else -> logger.warn("Processing for Update with type $type didn't implement")
    }
}

/**
 * Specific update from [Updates] with type [UpdateType.USER_ADDED]
 *
 * @param timestamp - Unix-time when event has occured
 * @param chatId - unique identifier of chat where user added
 * @param user - user added to chat
 * @param inviterId - user who added user to chat
 */
class UpdateUserAdded(
    override val type: UpdateType,
    val timestamp: Long,
    val chatId: ChatId,
    val user: User,
    val inviterId: UserId
) : Update {
    override suspend fun process(botScope: BotScope, logger: Logger) {
        botScope.userScope.answerOnAdd(AddedUserState(timestamp, chatId, user, inviterId))
    }
}

/**
 * Specific update from [Updates] with type [UpdateType.USER_REMOVED]
 *
 * @param timestamp - Unix-time when event has occured
 * @param chatId - unique identifier of chat where user removed
 * @param user - see bot API docs, this parameter means different, depends on event
 * @param adminId - Administrator who removed user from chat [UpdateType.USER_REMOVED]
 */
class UpdateUserRemoved(
    override val type: UpdateType,
    val timestamp: Long,
    val chatId: ChatId,
    val user: User,
    val adminId: UserId
) : Update {
    override suspend fun process(botScope: BotScope, logger: Logger) {
        botScope.userScope.answerOnRemove(RemovedUserState(timestamp, chatId, user, adminId))
    }
}

/**
 * Specific update from [Updates] with type [UpdateType.CHAT_TITLE_CHANGED]
 *
 * @param timestamp - Unix-time when event has occured
 * @param chatId - this chat id you will get in this event [UpdateType.BOT_STARTED], [UpdateType.CHAT_TITLE_CHANGED],
 * [UpdateType.USER_ADDED], [UpdateType.USER_REMOVED], [UpdateType.BOT_ADDED], [UpdateType.BOT_REMOVED]
 * @param user - see bot API docs, this parameter means different, depends on event
 * @param newChatTitle - changed title in chat [UpdateType.CHAT_TITLE_CHANGED]
 */
class UpdateChatTitle(
    override val type: UpdateType,
    val timestamp: Long,
    val chatId: ChatId,
    val user: User,
    val newChatTitle: String = ""
) : Update {
    override suspend fun process(botScope: BotScope, logger: Logger) {
        logger.warn("Processing for Update with type $type didn't implement")
    }
}

class UpdateUnknown(
    override val type: UpdateType,
    val timestamp: Long
) : Update {
    override suspend fun process(botScope: BotScope, logger: Logger) {
        logger.warn("Unknown type : Processing for Update with type $type didn't implement")
    }
}