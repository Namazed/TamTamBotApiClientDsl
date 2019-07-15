package chat.tamtam.botsdk.model.prepared

import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.MessageId
import chat.tamtam.botsdk.model.response.LinkType

data class Message(
    val body: Body,
    val recipient: Recipient,
    val sender: User,
    val timestamp: Long,
    val link: Link?,
    val statistics: Statistics
)

data class Body(
    val messageId: MessageId,
    val sequenceIdInChat: Long,
    val attachments: List<Attachment>,
    val text: String
)

data class Link(
    val type: LinkType,
    val sender: User,
    val chatId: ChatId,
    val body: Body
)

data class Statistics(
    val views: Int = -1
)