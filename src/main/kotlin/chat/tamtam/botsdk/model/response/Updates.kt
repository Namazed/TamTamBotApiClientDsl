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
import kotlinx.serialization.serializer
import kotlinx.serialization.withName

@Serializable
class Updates(
    @SerialName("updates") val listUpdates: List<Update>,
    @Optional val marker: Long = -1
)

@Serializable
class Update(
    val timestamp: Long,
    @SerialName("update_type") val updateType: UpdateType,
    @SerialName("message_id") @Optional val messageId: String = "",
    @SerialName("chat_id") @Optional val chatId: Long = -1,
    @SerialName("user_id") @Optional val userId: Long = -1,
    @SerialName("admin_id") @Optional val adminId: Long = -1,
    @SerialName("inviter_id") @Optional val inviterId: Long = -1,
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

    override fun deserialize(input: Decoder): UpdateType {
        return updateTypeFrom(input.decodeString())
    }

    @UseExperimental(ImplicitReflectionSerializer::class)
    override fun serialize(output: Encoder, obj: UpdateType) {
        UpdateType::class.serializer().serialize(output, obj)
    }
}