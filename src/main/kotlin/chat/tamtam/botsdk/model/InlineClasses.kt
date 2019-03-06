package chat.tamtam.botsdk.model

/**
 * Wrapper for chat id, it need for type safety
 */
inline class ChatId(val id: Long)

/**
 * look at [ChatId]
 */
inline class UserId(val id: Long)

/**
 * look at [ChatId]
 */
inline class BotId(val id: Long)

/**
 * look at [ChatId]
 */
inline class CallbackId(val id: String)

/**
 * look at [ChatId]
 */
inline class MessageId(val id: String)

/**
 * look at [ChatId]
 */
inline class Payload(val value: String)

/**
 * look at [ChatId]
 */
inline class ImageUrl(val value: String)

/**
 * look at [ChatId]
 */
inline class VideoUrl(val value: String)