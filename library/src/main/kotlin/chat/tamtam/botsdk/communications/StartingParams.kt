package chat.tamtam.botsdk.communications

/**
 * Use this class when you want start longPolling or webhook (in next release) process
 * @property botToken - token of your bot, you can give it from primeBot in TamTam
 * @property httpLogsEnabled - this flag mean that you want see http logs (when send some request) (by default false).
 * The SocketTimeoutException in httpLogs is normal for longPolling.
 */
interface StartingParams {
    val botToken: String
    val httpLogsEnabled: Boolean
}