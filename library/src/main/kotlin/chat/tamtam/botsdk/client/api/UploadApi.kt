package chat.tamtam.botsdk.client.api

import chat.tamtam.botsdk.model.request.UploadType
import chat.tamtam.botsdk.model.response.Upload
import chat.tamtam.botsdk.model.response.UploadInfo
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.Url

internal interface UploadApi {

    @Multipart
    @POST
    fun upload(
        @Url url: String,
        @Part part: MultipartBody.Part
    ): Call<UploadInfo>

    @POST("/uploads")
    fun uploadUrl(
        @Query("access_token") botToken: String,
        @Query("type") uploadType: UploadType
    ): Call<Upload>

}
