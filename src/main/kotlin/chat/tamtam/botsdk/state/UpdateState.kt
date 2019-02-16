package chat.tamtam.botsdk.state

import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.Command
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.response.Callback
import chat.tamtam.botsdk.model.response.Message

sealed class UpdateState

class StartedBotState(val timestamp: Long, val chatId: ChatId, val userId: UserId) : UpdateState()
class AddedBotState(val timestamp: Long, val chatId: ChatId, val userId: UserId) : UpdateState()
class RemovedBotState(val timestamp: Long, val chatId: ChatId, val userId: UserId) : UpdateState()
class AddedUserState(val timestamp: Long, val chatId: ChatId, val userId: UserId, val inviterId: UserId) : UpdateState()
class RemovedUserState(val timestamp: Long, val chatId: ChatId, val userId: UserId, val adminId: UserId) : UpdateState()
class CallbackState(val timestamp: Long, val callback: Callback) : UpdateState()
class MessageState(val timestamp: Long, val message: Message) : UpdateState()
class CommandState(val timestamp: Long, val command: Command) : UpdateState()