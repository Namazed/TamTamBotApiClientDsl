package chat.tamtam.botsdk.communications

/**
 * Use this class when you want start longPolling process
 * @param botToken - look in [StartingParams]
 * @param httpLogsEnabled - look in [StartingParams]
 * @param async - this flag mean that longPolling start polling on another single thread (by default false)
 * @param parallelWorkWithUpdates - this flag mean that all updates processed in parallel (by default true)
 */
class LongPollingStartingParams(
    override val botToken: String,
    override val httpLogsEnabled: Boolean = false,
    val async: Boolean = false,
    val parallelWorkWithUpdates: Boolean = true
) : StartingParams