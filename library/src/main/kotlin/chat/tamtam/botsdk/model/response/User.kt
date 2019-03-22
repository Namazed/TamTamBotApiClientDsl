package chat.tamtam.botsdk.model.response

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This class contains info about specific user or current bot
 *
 * @param userId - Users identifier
 * @param name - Users visible name
 * @param username - Unique public user name. Can be null if user is not accessible or it is not set
 * @param avatarUrl - URL of avatar
 * @param fullAvatarUrl - URL of avatar of a bigger size
 */
@Serializable
data class User(
    @SerialName("user_id") val userId: Long = -1,
    val name: String = "",
    @Optional val username: String = "",
    @SerialName("avatar_url") @Optional val avatarUrl: String = "",
    @SerialName("full_avatar_url") @Optional val fullAvatarUrl: String = ""
)