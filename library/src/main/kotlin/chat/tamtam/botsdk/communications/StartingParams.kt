package chat.tamtam.botsdk.communications

/**
 * Use this class when you want start longPolling or webhook (in next release) process
 * @param botToken - token of your bot, you can give it from primeBot in TamTam
 * @param httpLogsEnabled - this flag mean that you want see http logs (when send some request) (by default false).
 * The SocketTimeoutException in httpLogs is normal for longPolling.
 * @param async - this flag mean that longPolling start polling on another single thread (by default false)
 * @param parallelWorkWithUpdates - this flag mean that all updates processed in parallel (by default true)
 */
class StartingParams(
    val botToken: String,
    val httpLogsEnabled: Boolean = false,
    val async: Boolean = false,
    val parallelWorkWithUpdates: Boolean = true
)