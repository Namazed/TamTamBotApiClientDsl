package chat.tamtam.botsdk.model.request

import chat.tamtam.botsdk.model.request.Action.TYPING_ON
import kotlinx.serialization.Serializable

/**
 * Wrapper for action for Bot Api
 */
@Serializable
class ActionWrapper(
    val action: String
)

/**
 * This action describing what you do now in chat
 * Now this sdk support only [TYPING_ON]
 */
enum class Action {
    TYPING_ON,
    TYPING_OFF,
    SENDING_PHOTO,
    SENDING_VIDEO,
    SENDING_AUDIO,
    MARK_SEEN
}