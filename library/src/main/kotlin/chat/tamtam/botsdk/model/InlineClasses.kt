package chat.tamtam.botsdk.model

/**
 * Wrapper for chat id, it need for type safety
 */
inline class ChatId(val id: Long)

/**
 * @see [ChatId]
 */
inline class UserId(val id: Long)

/**
 * @see [ChatId]
 */
inline class BotId(val id: Long)

/**
 * @see [ChatId]
 */
inline class CallbackId(val id: String)

/**
 * @see [ChatId]
 */
inline class MessageId(val id: String)

/**
 * @see [ChatId]
 */
inline class ImageUrl(val value: String)