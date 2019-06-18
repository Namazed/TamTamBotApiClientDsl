package chat.tamtam.botsdk.client.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

internal object RetrofitFactory {
    private const val BASE_URL = "https://botapi.tamtam.chat"

    fun createRetrofit(url: String? = null, httpLogsEnabled: Boolean = false): Retrofit {
        val contentType = MediaType.get("application/json")
        return Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory(contentType))
            .baseUrl(url ?: BASE_URL)
            .client(createOkHttpClient(httpLogsEnabled))
            .build()
    }

    private fun createOkHttpClient(httpLogsEnabled: Boolean): OkHttpClient {
        OkHttpClient.Builder().apply {
            if (httpLogsEnabled) {
                val loggingInterceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
                addInterceptor(loggingInterceptor)
            }
            connectTimeout(20, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            return build()
        }
    }
}