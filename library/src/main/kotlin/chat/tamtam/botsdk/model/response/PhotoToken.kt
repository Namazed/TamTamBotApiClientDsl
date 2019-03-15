package chat.tamtam.botsdk.model.response

import kotlinx.serialization.Serializable

/**
 * This token you will get after uploaded Photo attachment.
 *
 * @param token - Encoded information of uploaded image
 */
@Serializable
class PhotoToken(
    val token: String
)