package chat.tamtam.botsdk.model.response

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Updates(
    @SerialName("updates") val listUpdates: List<Update>
)

@Serializable
class Update(
    val timestamp: Long,
    val updateType: String,
    val messageId: String = "",
    @Optional val message: Message = EMPTY_MESSAGE,
    @Optional val callback: Callback = EMPTY_CALLBACK
)

//sealed class UpdateState() {
//    class MessageCreatedState(message: Message)
//}