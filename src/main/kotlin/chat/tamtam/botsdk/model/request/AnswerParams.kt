package chat.tamtam.botsdk.model.request

import chat.tamtam.botsdk.model.CallbackId
import chat.tamtam.botsdk.model.UserId

class AnswerParams(
    val callbackId: CallbackId,
    val userId: UserId
)