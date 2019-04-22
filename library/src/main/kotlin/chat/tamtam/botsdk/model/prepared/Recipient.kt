package chat.tamtam.botsdk.model.prepared

import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.response.ChatType

class Recipient(
    val chatId: ChatId,
    val chatType: ChatType,
    val userId: UserId
)