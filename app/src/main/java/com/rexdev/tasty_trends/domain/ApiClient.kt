package com.rexdev.tasty_trends.domain

import android.util.Log
import com.google.gson.GsonBuilder
import com.rexdev.tasty_trends.global.GlobalVariables
import com.roydev.tastytrends.ApiService
import okhttp3.Cache
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

object RetrofitInstance {
    private val app = GlobalVariables
    private var baseUrl: String = app.HOST_URL // Initialize with the global HOST_URL
    private const val fallbackUrl1 = "http://192.168.100.51:80/"

    // Use the cache directory provided by the Android context
    private val cacheDir = File(app.cacheDir, "http_cache") // This should point to your app's cache directory
    private val cacheSize = 10 * 1024 * 1024 // 10 MB
    private val cache = Cache(cacheDir, cacheSize.toLong())

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val dynamicUrlInterceptor = DynamicUrlInterceptor()

    private var okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(dynamicUrlInterceptor)
        .cache(cache)
        .build()

    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .build()

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    fun setBaseUrl(newUrl: String) {
        if (newUrl.toHttpUrlOrNull() != null) {
            baseUrl = newUrl
            app.HOST_URL = newUrl
            rebuildRetrofit() // Rebuild Retrofit instance if needed
        } else {
            Log.e("RetrofitInstance", "Invalid URL: $newUrl")
        }
    }

    private fun rebuildRetrofit() {
        // Rebuild the Retrofit instance with the new base URL
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
    }

    private class DynamicUrlInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            var response: Response

            try {
                response = chain.proceed(request)
                Log.d("DynamicUrlInterceptor", "Response Code: ${response.code}")

                if (!response.isSuccessful) {
                    response.close()
                    switchToFallbackUrl()
                    response = chain.proceed(request)
                }
            } catch (e: Exception) {
                Log.e("DynamicUrlInterceptor", "Error occurred: ${e.message}")
                switchToFallbackUrl()
                response = chain.proceed(request)
            }

            return response
        }


        private fun switchToFallbackUrl() {
            baseUrl = fallbackUrl1
            app.HOST_URL = fallbackUrl1
            Log.w("DynamicUrlInterceptor", "Switched to fallback URL: $baseUrl")
            rebuildRetrofit() // Ensure Retrofit uses the new fallback URL
        }

        private fun rebuildRequestWithHeaderUrl(originalRequest: Request): Request {
            val newUrl = buildUrl(baseUrl, originalRequest.url)
            return originalRequest.newBuilder()
                .url(newUrl)
                .build()
        }

        private fun buildUrl(baseUrl: String, originalUrl: HttpUrl): HttpUrl {
            val httpUrl = createHttpUrl(baseUrl) ?: return originalUrl
            val trimmedPath = originalUrl.encodedPath.trimStart('/')

            Log.e("URLBuilder", "Constructed URL: ${httpUrl.toString()}/$trimmedPath")

            return HttpUrl.Builder()
                .scheme(httpUrl.scheme)
                .host(httpUrl.host)
                .port(httpUrl.port)
                .addPathSegments(trimmedPath)
                .build()
        }

        private fun createHttpUrl(urlString: String): HttpUrl? {
            return urlString.toHttpUrlOrNull()
        }
    }
}
