package chat.tamtam.botsdk.model.request

import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.UserId

class SendParams(
    val userId: UserId = UserId(-1L),
    val chatId: ChatId = ChatId(-1L),
    val sendMessage: SendMessage
)