package chat.tamtam.botsdk.model.prepared

import chat.tamtam.botsdk.model.UserId

/**
 * This class contains info about specific user or current bot
 *
 * @param userId - Users identifier, inline class [UserId]
 * @param name - Users visible name
 * @param username - Unique public user name. Can be null if user is not accessible or it is not set
 * @param avatarUrl - URL of avatar
 * @param fullAvatarUrl - URL of avatar of a bigger size
 */
data class User(
    val userId: UserId,
    val name: String,
    val username: String,
    val avatarUrl: String,
    val fullAvatarUrl: String
)