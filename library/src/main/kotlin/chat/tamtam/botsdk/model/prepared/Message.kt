package chat.tamtam.botsdk.model.prepared

import chat.tamtam.botsdk.model.MessageId

data class Message(
    val body: Body,
    val recipient: Recipient,
    val sender: User,
    val timestamp: Long,
    val link: Link?
)

data class Body(
    val messageId: MessageId,
    val sequenceIdInChat: Long,
    val attachments: List<Attachment>,
    val text: String
)

class Link(
    val type: String,
    val message: Message
)