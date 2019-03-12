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
enum class Action(val value: String) {
    TYPING_ON("typing_on"),
    TYPING_OFF("typing_off"),
    SENDING_PHOTO("sending_photo"),
    SENDING_VIDEO("sending_video"),
    SENDING_AUDIO("sending_audio"),
    MARK_SEEN("mark_seen")
}