package com.namazed.orthobot.botsdk.client.api

import chat.tamtam.botsdk.client.BOT_TOKEN_FIELD
import chat.tamtam.botsdk.model.CallbackId
import chat.tamtam.botsdk.model.request.AnswerCallback
import chat.tamtam.botsdk.model.request.AnswerNotificationCallback
import chat.tamtam.botsdk.model.response.Default
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

private const val ANSWERS_ENDPOINT = "/answers"

interface AnswerApi {

    @POST(ANSWERS_ENDPOINT)
    fun answerOnCallback(
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query("callback_id") callbackId: CallbackId,
        @Body answerCallback: AnswerCallback
    ): Call<Default>

    @POST(ANSWERS_ENDPOINT)
    fun answerOnCallback(
        @Query(BOT_TOKEN_FIELD) botToken: String,
        @Query("callback_id") callbackId: CallbackId,
        @Body answerCallback: AnswerNotificationCallback
    ): Call<Default>

}