package chat.tamtam.botsdk.client.retrofit

import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * The result of all requests, which contains Success and Failure
 * Generic is Response type
 * Failure contains [Response] which contains response type, and simple message "Body is empty" or "Response isn't successful"
 */
sealed class HttpResult<out R>
class Success<R>(val response: R) : HttpResult<R>()
class Failure<R>(val response: Response<R>, val message: String) : HttpResult<R>()

internal suspend fun <R : Any?> Call<R>.await(): HttpResult<R> {
    return suspendCancellableCoroutine { continuation ->
        enqueue(object : Callback<R> {
            override fun onResponse(call: Call<R>?, response: Response<R>) {
                continuation.resume(response.prepareResult())
            }

            override fun onFailure(call: Call<R>, throwable: Throwable) {
                if (continuation.isCancelled) return
                continuation.resumeWithException(throwable)
            }
        })

        continuation.invokeOnCancellation {
            try {
                cancel()
            } catch (ex: Throwable) {
                TODO("Should add log")
            }
        }
    }
}

private fun <R> Response<R>.prepareResult(): HttpResult<R> {
    return if (isSuccessful) {
        body()?.let {
            Success(it)
        } ?: Failure(this, "Body is empty")
    } else {
        Failure(this, "Response isn't successful")
    }
}
