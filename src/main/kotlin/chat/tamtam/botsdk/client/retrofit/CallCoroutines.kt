package com.namazed.orthobot.botsdk.client.retrofit

import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun <T : Any?> Call<T>.await(): Response<T> {
    return suspendCancellableCoroutine { continuation ->
        enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>?, response: Response<T>) {
                if (response.isSuccessful) {
                    continuation.resume(response)
                }
            }

            override fun onFailure(call: Call<T>, throwable: Throwable) {
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