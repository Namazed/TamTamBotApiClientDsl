package chat.tamtam.botsdk.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This class use when you want subscribe on [chat.tamtam.botsdk.model.response.Updates]
 *
 * @param url - WebHook not supported yet. Url which server will use when need send you new [chat.tamtam.botsdk.model.response.Updates]
 * @param updateTypes - Filter updates which you want get
 * @param version - Version of API
 */
@Serializable
data class Subscription(
    val url: String,
    @SerialName("update_types") val updateTypes: List<String>? = null,
    val version: String? = null
)

internal fun Subscription.isEmpty(): Boolean = this.url.isEmpty()