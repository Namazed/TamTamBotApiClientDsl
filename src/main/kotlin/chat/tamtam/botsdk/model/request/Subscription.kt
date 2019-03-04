package chat.tamtam.botsdk.model.request

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

/**
 * This class use when you want subscribe on [chat.tamtam.botsdk.model.response.Updates]
 *
 * @param url - WebHook not supported yet. Url which server will use when need send you new [chat.tamtam.botsdk.model.response.Updates]
 * @param filter - Not supported yet on server. Filter updates which you want get
 */
@Serializable
class Subscription(
    val url: String,
    @Optional val filter: String = ""
)