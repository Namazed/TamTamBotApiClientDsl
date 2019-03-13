package chat.tamtam.botsdk.model.response
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class BotInfo(
    val name: String,
    @SerialName("user_id") val botId: Long
)