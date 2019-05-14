package chat.tamtam.botsdk.communications

/** Use this class when you want start longPolling or webhook (in next release) process
 * @param botToken - token of your bot, you can give it from primeBot in TamTam
 * @param async - this flag mean that longPolling start polling on another single thread
 * @param httpLogsEnabled - this flag mean that you want see http logs (when send some request).
 * The SocketTimeoutException in httpLogs is normal for longPolling.
 */
class StartingParams(
    val botToken: String,
    val httpLogsEnabled: Boolean = false,
    val async: Boolean = false
)