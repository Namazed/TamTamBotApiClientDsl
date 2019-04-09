package chat.tamtam.botsdk.model.prepared

import chat.tamtam.botsdk.client.RequestsManager
import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.response.ChatType
import chat.tamtam.botsdk.model.response.Status


/**
 * You will get it after call [RequestsManager.getAllChats]
 *
 * @param chats - list of requested chats
 * @param marker - reference to the next page of requested chats
 */
class ChatsList(
    val chats: List<Chat>,
    val marker: Long?
)

/**
 * You will get it after call [RequestsManager.getAllChats] or [RequestsManager.getChat]
 *
 * @param chatId - chats identifier, inline class [ChatId]
 * @param type - type of chat. One of: dialog, chat, channel [ChatType]
 * @param status - chat status. One of: [Status]
 * @param title - visible title of chat
 * @param icon - icon of chat
 * @param lastEventTime - time of last event occured in chat
 * @param participantsCount - number of people in chat. Always 2 for dialog chat type
 * @param ownerId - identifier of chat owner. Visible only for chat admins
 * @param participants - participants in chat with time of last activity. Visible only for chat admins
 * @param public - is current chat publicly available. Always false for dialogs
 * @param linkOnChat - link on chat if it is public
 * @param description - chat description
 */
data class Chat(
    val chatId: ChatId,
    val type: ChatType,
    val status: Status,
    val title: String,
    val icon: ChatIcon,
    val lastEventTime: Long,
    val participantsCount: Int,
    val ownerId: Long,
    val participants: Map<Long, Long>,
    val public: Boolean,
    val linkOnChat: String,
    val description: String
)

/**
 * Icon of chat contains in [Chat]
 *
 * @param url - URL of image
 */
class ChatIcon(
    val url: String
)