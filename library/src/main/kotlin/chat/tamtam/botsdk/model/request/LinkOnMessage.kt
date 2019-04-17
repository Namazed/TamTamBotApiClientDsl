package chat.tamtam.botsdk.model.request

import chat.tamtam.botsdk.model.MessageId
import chat.tamtam.botsdk.model.response.LinkType
import kotlinx.serialization.CompositeEncoder
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.SerialClassDescImpl

@Serializable(with = LinkOnMessageSerializer::class)
class LinkOnMessage(
    val type: LinkType,
    @SerialName("mid") val messageId: MessageId
)

internal object LinkOnMessageSerializer : KSerializer<LinkOnMessage> {
    override val descriptor: SerialDescriptor = object : SerialClassDescImpl("LinkOnMessage") {
        init {
            addElement("type")
            addElement("mid")
        }
    }

    override fun serialize(encoder: Encoder, obj: LinkOnMessage) {
        val compositeOutput: CompositeEncoder = encoder.beginStructure(descriptor)
        compositeOutput.encodeStringElement(descriptor, 0, obj.type.value)
        compositeOutput.encodeStringElement(descriptor, 1, obj.messageId.id)
        compositeOutput.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): LinkOnMessage {
        return LinkOnMessage(LinkType.FORWARD, MessageId(""))
    }
}