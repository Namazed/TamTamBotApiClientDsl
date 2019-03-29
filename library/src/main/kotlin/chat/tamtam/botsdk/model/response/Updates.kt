package chat.tamtam.botsdk.model.response

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Optional
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.withName

/**
 * This class handle when some users use your bot.
 *
 * @param listUpdates - Page of updates
 * @param marker - Pointer to the next data page
 */
@Serializable
class Updates(
    @SerialName("updates") val listUpdates: List<Update>,
    @Optional val marker: Long? = null
)

/**
 * Specific update from [Updates]
 *
 * @param timestamp - Unix-time when event has occured
 * @param updateType - type of update see class [UpdateType]
 * @param messageId - this message id you will get if user removed or restored some message
 * ([UpdateType.MESSAGE_REMOVED], [UpdateType.MESSAGE_RESTORED])
 * @param chatId - this chat id you will get in this event [UpdateType.BOT_STARTED], [UpdateType.CHAT_TITLE_CHANGED],
 * [UpdateType.USER_ADDED], [UpdateType.USER_REMOVED], [UpdateType.BOT_ADDED], [UpdateType.BOT_REMOVED]
 * @param userId - see bot API docs, this parameter means different, depends on event
 * @param adminId - Administrator who removed user from chat [UpdateType.USER_REMOVED]
 * @param inviterId - User who added user to chat [UpdateType.USER_ADDED]
 * @param newChatTitle - changed title in chat [UpdateType.CHAT_TITLE_CHANGED]
 * @param message - new or edited message or message where exists botKeyboard [UpdateType.CALLBACK] [UpdateType.MESSAGE_CREATED],
 * [UpdateType.MESSAGE_EDITED], if message equals [EMPTY_MESSAGE], that mean message is null return from server
 * @param callback - this class you will get if user pressed on button [UpdateType.CALLBACK]
 */
@Serializable
data class Update(
    val timestamp: Long,
    @SerialName("update_type") val updateType: UpdateType,
    @SerialName("message_id") @Optional val messageId: String = "",
    @SerialName("chat_id") @Optional val chatId: Long = -1,
    @SerialName("user_id") @Optional val userId: Long = -1,
    @SerialName("admin_id") @Optional val adminId: Long = -1,
    @SerialName("inviter_id") @Optional val inviterId: Long = -1,
    @SerialName("title") @Optional val newChatTitle: String = "",
    @Optional val message: Message = EMPTY_MESSAGE,
    @Optional val callback: Callback = EMPTY_CALLBACK
)

@Serializable(UpdateTypeSerializer::class)
enum class UpdateType(val type: String) {
    CALLBACK("message_callback"),
    MESSAGE_CREATED("message_created"),
    MESSAGE_REMOVED("message_removed"),
    MESSAGE_EDITED("message_edited"),
    MESSAGE_RESTORED("message_restored"),
    BOT_ADDED("bot_added"),
    BOT_REMOVED("bot_removed"),
    USER_ADDED("user_added"),
    USER_REMOVED("user_removed"),
    BOT_STARTED("bot_started"),
    CHAT_TITLE_CHANGED("chat_title_changed"),
    UNKNOWN("unknown")
}

fun updateTypeFrom(value: String) = when(value) {
    "message_callback" -> UpdateType.CALLBACK
    "message_created" -> UpdateType.MESSAGE_CREATED
    "message_removed" -> UpdateType.MESSAGE_REMOVED
    "message_edited" -> UpdateType.MESSAGE_EDITED
    "message_restored" -> UpdateType.MESSAGE_RESTORED
    "bot_added" -> UpdateType.BOT_ADDED
    "bot_removed" -> UpdateType.BOT_REMOVED
    "user_added" -> UpdateType.USER_ADDED
    "user_removed" -> UpdateType.USER_REMOVED
    "bot_started" -> UpdateType.BOT_STARTED
    "chat_title_changed" -> UpdateType.CHAT_TITLE_CHANGED
    else -> UpdateType.UNKNOWN
}

internal object UpdateTypeSerializer : KSerializer<UpdateType> {
    override val descriptor: SerialDescriptor
        get() = StringDescriptor.withName("UpdateType")

    override fun deserialize(decoder: Decoder): UpdateType {
        return updateTypeFrom(decoder.decodeString())
    }

    @UseExperimental(ImplicitReflectionSerializer::class)
    override fun serialize(encoder: Encoder, obj: UpdateType) {
        //todo write custom serializer
    }
}