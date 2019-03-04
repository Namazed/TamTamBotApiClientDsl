package chat.tamtam.botsdk.model.response

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.list
import kotlinx.serialization.withName

@Serializable
class ChatsResult(
    @Serializable(ChatsSerializer::class) val chats: List<Chat>,
    val marker: Long
)

internal object ChatsSerializer : KSerializer<List<Chat>> {
    override val descriptor: SerialDescriptor
        get() = StringDescriptor.withName("Chats")

    override fun deserialize(input: Decoder): List<Chat> {
        return Chat.serializer().list.deserialize(input)
    }

    override fun serialize(output: Encoder, obj: List<Chat>) {
        Chat.serializer().list.serialize(output, obj)
    }
}