package chat.tamtam.botsdk.model.request

import chat.tamtam.botsdk.model.CallbackId
import chat.tamtam.botsdk.model.UserId

/**
 * Use this class when you want answer on Callback
 *
 * @param callbackId - inline class which contains callback id, unique identifier of keyboard in message
 * @param userId - Optional. Inline class which contains unique identifier of user. Use it if you want answer as notification,
 * if you want send replacement message (edit old message with keyboard) you not needed put this parameter
 */
class AnswerParams(
    val callbackId: CallbackId,
    val userId: UserId = UserId(-1L)
)