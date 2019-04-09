@file: JvmName("MappingHelper")
package chat.tamtam.botsdk.model

import chat.tamtam.botsdk.model.AttachType.AUDIO
import chat.tamtam.botsdk.model.AttachType.CONTACT
import chat.tamtam.botsdk.model.AttachType.FILE
import chat.tamtam.botsdk.model.AttachType.IMAGE
import chat.tamtam.botsdk.model.AttachType.INLINE_KEYBOARD
import chat.tamtam.botsdk.model.AttachType.LOCATION
import chat.tamtam.botsdk.model.AttachType.SHARE
import chat.tamtam.botsdk.model.AttachType.STICKER
import chat.tamtam.botsdk.model.AttachType.VIDEO
import chat.tamtam.botsdk.model.prepared.AttachmentContact
import chat.tamtam.botsdk.model.prepared.AttachmentKeyboard
import chat.tamtam.botsdk.model.prepared.AttachmentLocation
import chat.tamtam.botsdk.model.prepared.AttachmentMedia
import chat.tamtam.botsdk.model.prepared.AttachmentPhoto
import chat.tamtam.botsdk.model.prepared.Body
import chat.tamtam.botsdk.model.prepared.ChatIcon
import chat.tamtam.botsdk.model.prepared.ChatMembersList
import chat.tamtam.botsdk.model.prepared.ChatsList
import chat.tamtam.botsdk.model.prepared.PayloadContact
import chat.tamtam.botsdk.model.prepared.PayloadKeyboard
import chat.tamtam.botsdk.model.prepared.PayloadMedia
import chat.tamtam.botsdk.model.prepared.PayloadPhoto
import chat.tamtam.botsdk.model.response.Attachment
import chat.tamtam.botsdk.model.response.Callback
import chat.tamtam.botsdk.model.response.Chat
import chat.tamtam.botsdk.model.response.ChatMember
import chat.tamtam.botsdk.model.response.ChatMembersResult
import chat.tamtam.botsdk.model.response.ChatsResult
import chat.tamtam.botsdk.model.response.Link
import chat.tamtam.botsdk.model.response.Message
import chat.tamtam.botsdk.model.response.Messages
import chat.tamtam.botsdk.model.response.Permissions
import chat.tamtam.botsdk.model.response.Recipient
import chat.tamtam.botsdk.model.response.User
import chat.tamtam.botsdk.model.prepared.Attachment as PreparedAttachment
import chat.tamtam.botsdk.model.prepared.Callback as PreparedCallback
import chat.tamtam.botsdk.model.prepared.Chat as PreparedChat
import chat.tamtam.botsdk.model.prepared.ChatMember as PreparedChatMember
import chat.tamtam.botsdk.model.prepared.Link as PreparedLink
import chat.tamtam.botsdk.model.prepared.Message as PreparedMessage
import chat.tamtam.botsdk.model.prepared.Recipient as PreparedRecipient
import chat.tamtam.botsdk.model.prepared.User as PreparedUser

internal fun User.map(): PreparedUser = PreparedUser(UserId(userId), name, username, avatarUrl, fullAvatarUrl)

internal fun ChatMember.map(): PreparedChatMember = PreparedChatMember(PreparedUser(UserId(userId), name, username, avatarUrl,
    fullAvatarUrl), lastAccessTime, isOwner, isAdmin, joinTime, permissions.mapPermissions())

internal fun ChatMembersResult.map(): ChatMembersList {
    val list = members.asSequence()
        .map(ChatMember::map)
        .toList()
    return ChatMembersList(list, marker)
}

private fun List<String>?.mapPermissions(): List<Permissions> {
    return this?.let {
        it.asSequence()
            .map { s: String -> Permissions.valueOf(s) }
            .toList()
    } ?: emptyList()
}

internal fun Recipient.map(): PreparedRecipient = PreparedRecipient(ChatId(chatId), chatType, UserId(userId))

internal fun Link.map(): PreparedLink = PreparedLink(type, message.map())

internal fun Message.map(): PreparedMessage {
    return PreparedMessage(Body(MessageId(messageInfo.messageId), messageInfo.sequenceIdInChat,
        messageInfo.attachments.map(), messageInfo.text), recipient.map(), sender.map(), timestamp, link?.map())
}

internal fun Message?.mapOrNull(): PreparedMessage? {
    return this?.map()
}

internal fun Messages.map(): List<PreparedMessage> = messages.asSequence()
    .map(Message::map)
    .toList()

internal fun Callback.map(): PreparedCallback = PreparedCallback(timestamp, CallbackId(callbackId), payload, user.map())

internal fun Attachment.map(): PreparedAttachment {
    return when (type) {
        IMAGE -> AttachmentPhoto(type, PayloadPhoto(payload.photoId, payload.token, payload.url))
        VIDEO, AUDIO, FILE, STICKER, SHARE -> AttachmentMedia(type, PayloadMedia(payload.url))
        CONTACT -> AttachmentContact(type, PayloadContact(payload.vcfInfo, payload.tamInfo.map()))
        INLINE_KEYBOARD -> AttachmentKeyboard(type, CallbackId(callbackId), PayloadKeyboard(payload.buttons))
        LOCATION -> AttachmentLocation(type, latitude, longitude)
    }
}

internal fun List<Attachment>.map(): List<PreparedAttachment> = map { attachment -> attachment.map() }

internal fun Chat.map(): PreparedChat = PreparedChat(ChatId(chatId), type, status, title, ChatIcon(icon.url), lastEventTime,
    participantsCount, ownerId, participants, public, linkOnChat, description)

internal fun ChatsResult.map(): ChatsList {
    val list = chats.asSequence()
        .map(Chat::map)
        .toList()
    return ChatsList(list, marker)
}

internal inline fun <reified R, reified PR> R.map(): PR {
    return when(this) {
        is User -> this.map() as PR
        is Message -> this.map() as PR
        is Messages -> this.map() as PR
        is ChatMember -> this.map() as PR
        is ChatMembersResult -> this.map() as PR
        is Chat -> this.map() as PR
        is ChatsResult -> this.map() as PR
        is Callback -> this.map() as PR
        else -> this as PR
    }
}