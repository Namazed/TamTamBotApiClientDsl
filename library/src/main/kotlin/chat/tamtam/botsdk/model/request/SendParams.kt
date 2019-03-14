package chat.tamtam.botsdk.model.request

import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.UserId

/**
 * Use this class when you want send message to user or to chat
 * In general this class use only with [chat.tamtam.botsdk.scopes.Scope.prepareFor] as result and use like Receiver
 * in other extensions methods [chat.tamtam.botsdk.scopes.Scope.sendWith]
 *
 * @param userId - Optional. Inline class which contains unique identifier of user. Use it if you want send message to user in dialog
 * @param chatId - Optional. Inline class which contains unique identifier of chat. Use it if you want send message to chat
 * @param sendMessage - message which you want send
 */
class SendParams(
    val userId: UserId = UserId(-1L),
    val chatId: ChatId = ChatId(-1L),
    val sendMessage: SendMessage
)