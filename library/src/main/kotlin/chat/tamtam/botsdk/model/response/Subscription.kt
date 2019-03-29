package chat.tamtam.botsdk.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * You will get this class if you call
 */
@Serializable
class Subscription(
    val url: String,
    @SerialName("time") val creationTime: Long,
    @SerialName("update_types") val updateTypes: List<UpdateType>? = null,
    val version: String
)