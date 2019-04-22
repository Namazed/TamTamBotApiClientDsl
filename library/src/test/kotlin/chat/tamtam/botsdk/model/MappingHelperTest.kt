package chat.tamtam.botsdk.model

import chat.tamtam.botsdk.model.prepared.AttachmentContact
import chat.tamtam.botsdk.model.prepared.AttachmentFile
import chat.tamtam.botsdk.model.prepared.AttachmentKeyboard
import chat.tamtam.botsdk.model.prepared.AttachmentLocation
import chat.tamtam.botsdk.model.prepared.AttachmentMedia
import chat.tamtam.botsdk.model.prepared.AttachmentPhoto
import chat.tamtam.botsdk.model.prepared.Body
import chat.tamtam.botsdk.model.response.Attachment
import chat.tamtam.botsdk.model.response.Callback
import chat.tamtam.botsdk.model.response.Chat
import chat.tamtam.botsdk.model.response.ChatIcon
import chat.tamtam.botsdk.model.response.ChatMember
import chat.tamtam.botsdk.model.response.ChatMembersResult
import chat.tamtam.botsdk.model.response.ChatType
import chat.tamtam.botsdk.model.response.Link
import chat.tamtam.botsdk.model.response.Message
import chat.tamtam.botsdk.model.response.MessageInfo
import chat.tamtam.botsdk.model.response.Messages
import chat.tamtam.botsdk.model.response.Payload
import chat.tamtam.botsdk.model.response.Recipient
import chat.tamtam.botsdk.model.response.SendMessage
import chat.tamtam.botsdk.model.response.Status
import chat.tamtam.botsdk.model.response.User
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MappingHelperTest {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    private fun <Value, PreparedValue> getAssertString(
        start: String,
        middle: String,
        value: Value,
        preparedValue: PreparedValue
    ): String {
        return "$start :$value: must be equals with $middle in prepared model :$preparedValue: after mapping"
    }

    @Test
    fun `check that list of userIds correct map to list longs`() {
        val userIds: List<UserId>? = null
        val longs = userIds.mapOrNull()
        assert(longs == null) {
            "List of longs, must be null after mapping"
        }
    }

    @Test
    fun `check that list of userIds correct map if list is null`() {
        val userIds = listOf(UserId(-1), UserId(-2), UserId(-3), UserId(12))
        val longs = userIds.mapOrNull()
        assert(!longs.isNullOrEmpty()) {
            "List of longs, doesn't must be empty or null after mapping"
        }
    }

    @Test
    fun `check that User correct map to User from prepared package`() {
        val user = User(21, "Test", "N", "testurl", "testfullurl")
        val preparedUser = user.map()
        user.assertEqualsUser(preparedUser)
    }

    private fun User.assertEqualsUser(
        preparedUser: chat.tamtam.botsdk.model.prepared.User
    ) {
        assert(userId == preparedUser.userId.id) {
            getAssertString("UserId of User", "userId", userId, preparedUser.userId)
        }
        assert(name == preparedUser.name) {
            getAssertString("Name of User", "name", name, preparedUser.name)
        }
        assert(username == preparedUser.username) {
            getAssertString("Username of User", "username", username, preparedUser.username)
        }
        assert(avatarUrl == preparedUser.avatarUrl) {
            getAssertString("AvatarUrl from User", "avatarUrl", avatarUrl, preparedUser.avatarUrl)
        }
        assert(fullAvatarUrl == preparedUser.fullAvatarUrl) {
            getAssertString("FullAvatarUrl from User", "fullAvatarUrl", fullAvatarUrl, preparedUser.fullAvatarUrl)
        }
    }

    @Test
    fun `check that ChatMember correct map to ChatMember from prepared package`() {
        val chatMember = ChatMember(
            21,
            "Test",
            "N",
            "testurl",
            "testfullurl",
            102L,
            isOwner = true,
            isAdmin = true,
            joinTime = 21L,
            permissions = listOf("read_all_messages", "write", "pin_message")
        )
        val preparedChatMember = chatMember.map()
        chatMember.assertEqualsChatMember(preparedChatMember)
    }

    private fun ChatMember.assertEqualsChatMember(
        preparedChatMember: chat.tamtam.botsdk.model.prepared.ChatMember
    ) {
        assert(userId == preparedChatMember.userInfo.userId.id) {
            getAssertString("UserId of ChatMember", "userId", userId, preparedChatMember.userInfo.userId)
        }
        assert(name == preparedChatMember.userInfo.name) {
            getAssertString("Name of ChatMember", "name", name, preparedChatMember.userInfo.name)
        }
        assert(username == preparedChatMember.userInfo.username) {
            getAssertString("Username of ChatMember", "username", username, preparedChatMember.userInfo.username)
        }
        assert(avatarUrl == preparedChatMember.userInfo.avatarUrl) {
            getAssertString("AvatarUrl of ChatMember", "avatarUrl", avatarUrl, preparedChatMember.userInfo.avatarUrl)
        }
        assert(fullAvatarUrl == preparedChatMember.userInfo.fullAvatarUrl) {
            getAssertString(
                "FullAvatarUrl of ChatMember",
                "fullAvatarUrl",
                fullAvatarUrl,
                preparedChatMember.userInfo.fullAvatarUrl
            )
        }
        assert(lastAccessTime == preparedChatMember.lastAccessTime) {
            getAssertString(
                "LastAccessTime of ChatMember",
                "lastAccessTime",
                lastAccessTime,
                preparedChatMember.lastAccessTime
            )
        }
        assert(isOwner == preparedChatMember.isOwner) {
            getAssertString("IsOwner from ChatMember", "isOwner", isOwner, preparedChatMember.isOwner)
        }
        assert(isAdmin == preparedChatMember.isAdmin) {
            getAssertString("IsAdmin from ChatMember", "isAdmin", isAdmin, preparedChatMember.isAdmin)
        }
        assert(joinTime == preparedChatMember.joinTime) {
            getAssertString("JoinTime from ChatMember", "joinTime", joinTime, preparedChatMember.joinTime)
        }
        assert(permissions?.size == preparedChatMember.permissions?.size) {
            getAssertString(
                "Permissions.size from ChatMember",
                "permissions.size",
                permissions?.size,
                preparedChatMember.permissions?.size
            )
        }
    }

    @Test
    fun `check that list of string permissions correct map to list of enum permissions`() {
        val stringPermissions = listOf("read_all_messages", "write", "pin_message")
        val permissions = stringPermissions.mapPermissions()
        assert(stringPermissions.size == permissions.size) {
            getAssertString(
                "Permissions.size from list of string permissions",
                "permissions.size",
                stringPermissions.size,
                permissions.size
            )
        }
        assert(stringPermissions[0] == permissions[0].type) {
            getAssertString(
                "Permissions first element from list of string permission",
                "permissions first element",
                stringPermissions[0],
                permissions[0]
            )
        }
        assert(stringPermissions[1] == permissions[1].type) {
            getAssertString(
                "Permissions second element from list of string permission",
                "permissions second element",
                stringPermissions[1],
                permissions[1]
            )
        }
        assert(stringPermissions[2] == permissions[2].type) {
            getAssertString(
                "Permissions third element from list of string permission",
                "permissions third element",
                stringPermissions[2],
                permissions[2]
            )
        }
    }

    @Test
    fun `check that ChatMembersResult correct map to ChatMembersList from prepared package`() {
        val chatMember = ChatMember(
            21,
            "Test",
            "N",
            "testurl",
            "testfullurl",
            102L,
            isOwner = true,
            isAdmin = true,
            joinTime = 21L,
            permissions = listOf("read_all_messages", "write", "pin_message")
        )
        val chatMembersResult = ChatMembersResult(listOf(chatMember), 221L)
        val chatMembersList = chatMembersResult.map()
        chatMembersResult.members[0].assertEqualsChatMember(chatMembersList.members[0])
        assert(chatMembersResult.marker == chatMembersList.marker) {
            getAssertString("Marker of ChatMembersResult", "marker", chatMembersResult.marker, chatMembersList.marker)
        }
    }

    @Test
    fun `check that Recipient correct map to Recipient from prepared package`() {
        val recipient = Recipient(21, ChatType.CHAT, 41)
        val preparedRecipient = recipient.map()
        recipient.assertEqualsRecipient(preparedRecipient)
    }

    private fun Recipient.assertEqualsRecipient(
        preparedRecipient: chat.tamtam.botsdk.model.prepared.Recipient
    ) {
        assert(chatId == preparedRecipient.chatId.id) {
            getAssertString("ChatId of Recipient", "chatId", chatId, preparedRecipient.chatId)
        }
        assert(chatType == preparedRecipient.chatType) {
            getAssertString("ChatType of Recipient", "chatType", chatType, preparedRecipient.chatType)
        }
        assert(userId == preparedRecipient.userId.id) {
            getAssertString("UserId of Recipient", "userId", userId, preparedRecipient.userId)
        }
    }

    @Test
    fun `check that Link correct map to Link from prepared package`() {
        val link = Link(
            "reply", User(
                21L, "vasya", "awesomeVasya", "kakoitourl.com",
                "fullkakakoito.url"
            ), 51L, MessageInfo("31613sasf", 23, listOf(), "bla bla dracula")
        )
        val preparedLink = link.map()
        link.assertEqualsLink(preparedLink)
    }

    private fun Link.assertEqualsLink(
        preparedLink: chat.tamtam.botsdk.model.prepared.Link
    ) {
        assert(type == preparedLink.type.value) {
            getAssertString("Type of Link", "type", type, preparedLink.type.value)
        }
        assert(chatId == preparedLink.chatId.id) {
            getAssertString("ChatId of Link", "chatId", chatId, preparedLink.chatId)
        }
        sender.assertEqualsUser(preparedLink.sender)
        message.assertEqualsMessageInfo(preparedLink.body)
    }

    @Test
    fun `check that MessageInfo correct map to Body from prepared package`() {
        val messageInfo = MessageInfo("31613sasf", 23, listOf(), "bla bla dracula")
        val body = messageInfo.map()
        messageInfo.assertEqualsMessageInfo(body)
    }

    private fun MessageInfo.assertEqualsMessageInfo(
        body: Body
    ) {
        assert(messageId == body.messageId.id) {
            getAssertString("MessageId of MessageInfo", "type", messageId, body.messageId)
        }
        assert(sequenceIdInChat == body.sequenceIdInChat) {
            getAssertString(
                "SequenceIdInChat from MessageInfo",
                "sequenceIdInChat",
                sequenceIdInChat,
                body.sequenceIdInChat
            )
        }
        assert(attachments.size == body.attachments.size) {
            getAssertString(
                "Attachments.size of MessageInfo",
                "attachments.size",
                attachments.size,
                body.attachments.size
            )
        }
        assert(text == body.text) {
            getAssertString("Text of MessageInfo", "text", text, body.text)
        }
    }

    @Test
    fun `check that Message correct map to Message from prepared package`() {
        val link = Link(
            "reply", User(
                21L, "vasya", "awesomeVasya", "kakoitourl.com",
                "fullkakakoito.url"
            ), 51L, MessageInfo("31613sasf", 23, listOf(), "bla bla dracula")
        )
        val message = Message(
            MessageInfo("31613sasf", 23, listOf(), "bla bla dracula"),
            Recipient(12L, ChatType.CHAT, 35L),
            User(21L, "vasya", "awesomeVasya", "kakoitourl.com", "fullkakakoito.url"),
            125152L, link
        )
        val preparedMessage = message.map()
        message.assertEqualsMessage(preparedMessage)
    }

    private fun Message.assertEqualsMessage(
        preparedMessage: chat.tamtam.botsdk.model.prepared.Message
    ) {
        messageInfo.assertEqualsMessageInfo(preparedMessage.body)
        recipient.assertEqualsRecipient(preparedMessage.recipient)
        sender.assertEqualsUser(preparedMessage.sender)
        link?.assertEqualsLink(preparedMessage.link!!)
    }

    @Test
    fun `check that Message correct map to null if message is null`() {
        val message: Message? = null
        val preparedMessage = message.mapOrNull()
        assert(preparedMessage == null) {
            "If message before mapping was null, that after mapping preparedMessage must be null too"
        }
    }

    @Test
    fun `check that Messages correct map to list of messages`() {
        val message = Message(
            MessageInfo("31613sasf", 23, listOf(), "bla bla dracula"),
            Recipient(12L, ChatType.CHAT, 35L),
            User(21L, "vasya", "awesomeVasya", "kakoitourl.com", "fullkakakoito.url"),
            125152L, null
        )
        val messageTwo = Message(
            MessageInfo("xvaf", 32, listOf(), "bla dracula"),
            Recipient(12L, ChatType.CHAT, 35L),
            User(2L, "vasya", "awesome", "kakoito.com", "fullkaka.url"),
            1252L, null
        )
        val messages = Messages(listOf(message, messageTwo))
        val listOfMessages = messages.map()
        assert(messages.messages.size == listOfMessages.size) {
            getAssertString("Messages.size from Messages", "messages.size", messages.messages.size, listOfMessages.size)
        }
        messages.messages[0].assertEqualsMessage(listOfMessages[0])
        messages.messages[1].assertEqualsMessage(listOfMessages[1])
    }

    @Test
    fun `check that SendMessage correct map to Message from prepared package`() {
        val message = Message(
            MessageInfo("31613sasf", 23, listOf(), "bla bla dracula"),
            Recipient(12L, ChatType.CHAT, 35L),
            User(21L, "vasya", "awesomeVasya", "kakoitourl.com", "fullkakakoito.url"),
            125152L, null
        )
        val sendMessage = SendMessage(message)
        val preparedMessage = sendMessage.map()
        sendMessage.message.assertEqualsMessage(preparedMessage)
    }

    @Test
    fun `check that Callback correct map to Callback from prepared package`() {
        val callback = Callback(
            15235L,
            "casgasg",
            "payload",
            User(21L, "vasya", "awesomeVasya", "kakoitourl.com", "fullkakakoito.url")
        )
        val preparedCallback = callback.map()
        assert(callback.callbackId == preparedCallback.callbackId.id) {
            getAssertString("CallbackId of Callback", "callbackId", callback.callbackId, preparedCallback.callbackId)
        }
        assert(callback.payload == preparedCallback.payload) {
            getAssertString("Payload of Callback", "payload", callback.payload, preparedCallback.payload)
        }
        assert(callback.timestamp == preparedCallback.timestamp) {
            getAssertString("Timestamp of Callback", "timestamp", callback.timestamp, preparedCallback.timestamp)
        }
        callback.user.assertEqualsUser(preparedCallback.user)
    }

    @Test
    fun `check that Attachment with image type correct map to AttachmentPhoto from prepared package`() {
        val attachment = Attachment(AttachType.IMAGE, payload = Payload(12L, "token", "url"))
        val preparedAttachment = attachment.map() as AttachmentPhoto
        assert(attachment.type == preparedAttachment.type) {
            getAssertString("Type of Attachment", "type", attachment.type, preparedAttachment.type)
        }
        assert(attachment.payload.photoId == preparedAttachment.payload.photoId) {
            getAssertString(
                "PhotoId of Attachment",
                "photoId",
                attachment.payload.photoId,
                preparedAttachment.payload.photoId
            )
        }
        assert(attachment.payload.token == preparedAttachment.payload.token) {
            getAssertString("Token of Attachment", "token", attachment.payload.token, preparedAttachment.payload.token)
        }
        assert(attachment.payload.url == preparedAttachment.payload.url) {
            getAssertString("Url of Attachment", "url", attachment.payload.url, preparedAttachment.payload.url)
        }
    }

    @Test
    fun `check that Attachment with video type correct map to AttachmentMedia from prepared package`() {
        val attachment = Attachment(AttachType.VIDEO, payload = Payload(url = "url", id = 21))
        val preparedAttachment = attachment.map() as AttachmentMedia
        assert(attachment.type == preparedAttachment.type) {
            getAssertString("Type of Attachment", "type", attachment.type, preparedAttachment.type)
        }
        assert(attachment.payload.url == preparedAttachment.payload.url) {
            getAssertString("Url of Attachment", "url", attachment.payload.url, preparedAttachment.payload.url)
        }
        assert(attachment.payload.id == preparedAttachment.payload.id) {
            getAssertString("Id of Attachment", "id", attachment.payload.id, preparedAttachment.payload.id)
        }
    }

    @Test
    fun `check that Attachment with file type correct map to AttachmentFile from prepared package`() {
        val attachment = Attachment(AttachType.FILE, payload = Payload(url = "url", id = 32))
        val preparedAttachment = attachment.map() as AttachmentFile
        assert(attachment.type == preparedAttachment.type) {
            getAssertString("Type of Attachment", "type", attachment.type, preparedAttachment.type)
        }
        assert(attachment.payload.url == preparedAttachment.payload.url) {
            getAssertString("Url of Attachment", "url", attachment.payload.url, preparedAttachment.payload.url)
        }
        assert(attachment.payload.fileId == preparedAttachment.payload.fileId) {
            getAssertString("FileId of Attachment", "fileId", attachment.payload.fileId, preparedAttachment.payload.fileId)
        }
    }

    @Test
    fun `check that Attachment with contact type correct map to AttachmentContact from prepared package`() {
        val attachment = Attachment(AttachType.CONTACT, payload = Payload(vcfInfo = "vcfInfo", tamInfo = User()))
        val preparedAttachment = attachment.map() as AttachmentContact
        assert(attachment.type == preparedAttachment.type) {
            getAssertString("Type of Attachment", "type", attachment.type, preparedAttachment.type)
        }
        assert(attachment.payload.vcfInfo == preparedAttachment.payload.vcfInfo) {
            getAssertString(
                "VcfInfo of Attachment",
                "vcfInfo",
                attachment.payload.vcfInfo,
                preparedAttachment.payload.vcfInfo
            )
        }
        attachment.payload.tamInfo.assertEqualsUser(preparedAttachment.payload.tamInfo)
    }

    @Test
    fun `check that Attachment with inline keyboard type correct map to AttachmentKeyboard from prepared package`() {
        val attachment = Attachment(AttachType.INLINE_KEYBOARD, payload = Payload(buttons = listOf()))
        val preparedAttachment = attachment.map() as AttachmentKeyboard
        assert(attachment.type == preparedAttachment.type) {
            getAssertString("Type of Attachment", "type", attachment.type, preparedAttachment.type)
        }
        assert(attachment.payload.buttons.size == preparedAttachment.payload.buttons.size) {
            getAssertString(
                "Buttons.size of Attachment",
                "buttons.size",
                attachment.payload.buttons.size,
                preparedAttachment.payload.buttons.size
            )
        }
    }

    @Test
    fun `check that Attachment with location type correct map to AttachmentLocation from prepared package`() {
        val attachment = Attachment(AttachType.LOCATION, latitude = 12.03, longitude = 56.01)
        val preparedAttachment = attachment.map() as AttachmentLocation
        attachment.assertEqualsAttachmentLocation(preparedAttachment)
    }

    private fun Attachment.assertEqualsAttachmentLocation(
        preparedAttachment: AttachmentLocation
    ) {
        assert(type == preparedAttachment.type) {
            getAssertString("Type of Attachment", "type", type, preparedAttachment.type)
        }
        assert(latitude == preparedAttachment.latitude) {
            getAssertString("Latitude of Attachment", "latitude", latitude, preparedAttachment.latitude)
        }
        assert(longitude == preparedAttachment.longitude) {
            getAssertString("Longitude of Attachment", "longitude", longitude, preparedAttachment.longitude)
        }
    }

    @Test
    fun `check that list of Attachments with different types correct map to list of Attachments from prepared package`() {
        val attachmentLocation = Attachment(AttachType.LOCATION)
        val attachmentVideo = Attachment(AttachType.VIDEO)
        val attachmentImage = Attachment(AttachType.IMAGE)
        val attachmentContact = Attachment(AttachType.CONTACT)
        val attachmentKeyboard = Attachment(AttachType.INLINE_KEYBOARD)
        val attachments =
            listOf(attachmentLocation, attachmentVideo, attachmentImage, attachmentContact, attachmentKeyboard)
        val preparedAttachment = attachments.map()
        assert(attachments.size == preparedAttachment.size) {
            getAssertString("Size of Attachments", "size", attachments.size, preparedAttachment.size)
        }
        attachments[0].assertEqualsAttachmentLocation(preparedAttachment[0] as AttachmentLocation)
    }

    @Test
    fun `check that Chat correct map to Chat from prepared package`() {
        val chat = Chat(
            125L, ChatType.CHAT, Status.ACTIVE, "kakto tak", ChatIcon("asf"), 12,
            4, 12511255L, public = true, linkOnChat = "link", description = "kakto tak da"
        )
        val preparedChat = chat.map()
        chat.assertEqualsChat(preparedChat)
    }

    private fun Chat.assertEqualsChat(preparedChat: chat.tamtam.botsdk.model.prepared.Chat) {
        assert(chatId == preparedChat.chatId.id) {
            getAssertString("ChatId of Chat", "chatId", chatId, preparedChat.chatId)
        }
        assert(type == preparedChat.type) {
            getAssertString("Type of Chat", "type", type, preparedChat.type)
        }
        assert(status == preparedChat.status) {
            getAssertString("Status of Chat", "status", status, preparedChat.status)
        }
        assert(title == preparedChat.title) {
            getAssertString("Title of Chat", "title", title, preparedChat.title)
        }
        assert(icon?.url == preparedChat.icon.url) {
            getAssertString("ChatIcon.url of Chat", "chatIcon.url", icon?.url, preparedChat.icon.url)
        }
        assert(lastEventTime == preparedChat.lastEventTime) {
            getAssertString("LastEventTime of Chat", "lastEventTime", lastEventTime, preparedChat.lastEventTime)
        }
        assert(participantsCount == preparedChat.participantsCount) {
            getAssertString("ParticipantsCount of Chat", "participantsCount", participantsCount, preparedChat.participantsCount)
        }
        assert(ownerId == preparedChat.ownerId) {
            getAssertString("OwnerId of Chat", "ownerId", ownerId, preparedChat.ownerId)
        }
        assert(participants.size == preparedChat.participants.size) {
            getAssertString("Participants.size of Chat", "participants.size", participants.size, preparedChat.participants.size)
        }
        assert(public == preparedChat.public) {
            getAssertString("Public of Chat", "public", public, preparedChat.public)
        }
        assert(linkOnChat == preparedChat.linkOnChat) {
            getAssertString("LinkOnChat of Chat", "linkOnChat", linkOnChat, preparedChat.linkOnChat)
        }
        assert(description == preparedChat.description) {
            getAssertString("Description of Chat", "description", description, preparedChat.description)
        }
    }

}