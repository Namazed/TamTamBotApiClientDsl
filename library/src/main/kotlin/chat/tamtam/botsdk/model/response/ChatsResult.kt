package chat.tamtam.botsdk.model.response

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.list
import kotlinx.serialization.withName

/**
 * You will get it after call [chat.tamtam.botsdk.client.RequestsManager.getAllChats]
 *
 * @param chats - list of requested chats
 * @param marker - reference to the next page of requested chats
 */
@Serializable
class ChatsResult(
    @Serializable(ChatsSerializer::class) val chats: List<Chat>,
    val marker: Long? = null
)

internal object ChatsSerializer : KSerializer<List<Chat>> {
    override val descriptor: SerialDescriptor
        get() = StringDescriptor.withName("Chats")

    override fun deserialize(decoder: Decoder): List<Chat> {
        return Chat.serializer().list.deserialize(decoder)
    }

    override fun serialize(encoder: Encoder, obj: List<Chat>) {
        Chat.serializer().list.serialize(encoder, obj)
    }
}