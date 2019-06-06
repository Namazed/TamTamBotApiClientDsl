package chat.tamtam.botsdk.model.request

import chat.tamtam.botsdk.model.response.PhotoToken
import kotlinx.serialization.Serializable

/**
 * This class need if you want change Icon for Chat.
 *
 * @param url - set icon from url
 * @param photos - set icon from uploaded image, you can get this parameter after you upload image on server
 */
@Serializable
class Icon(
   val url: String? = null,
   val photos: Map<String, PhotoToken>? = null
)