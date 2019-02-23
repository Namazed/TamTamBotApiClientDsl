package chat.tamtam.botsdk.model.request

import kotlinx.serialization.Serializable

@Serializable
class ActionWrapper(
    val action: String
)

enum class Action {
    TYPING_ON,
    TYPING_OFF,
    SENDING_PHOTO,
    SENDING_VIDEO,
    SENDING_AUDIO,
    MARK_SEEN
}