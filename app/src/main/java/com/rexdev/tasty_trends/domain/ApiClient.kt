package com.rexdev.tasty_trends.domain

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.GsonBuilder
import com.rexdev.tasty_trends.global.GlobalVariables
import com.roydev.tastytrends.ApiService
import okhttp3.Cache
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import java.io.IOException
import org.json.JSONException
import org.json.JSONObject

object RetrofitInstance {
    private val app = GlobalVariables
    //private lateinit var sharedPreferences: SharedPreferences
    //private const val PREFS_NAME = "TastyTrendsPrefs"
    //private const val LAST_USED_URL_KEY = "last_used_url"

    // Base URL for your API (static, no switching)
    private var baseUrl: String = "http://192.168.149.197:80/"

    // Imgur-specific constants
    private const val IMGUR_BASE_URL = "https://api.imgur.com/3/"
    private const val IMGUR_CLIENT_ID = "8b791601ce81511"

    // Cache and logging setup
    private val cacheDir = File(app.cacheDir, "http_cache")
    private val cacheSize = 10 * 1024 * 1024 // 10 MB
    private val cache = Cache(cacheDir, cacheSize.toLong())

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private var okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .cache(cache)
        .build()

    private var retrofit: Retrofit = createRetrofit(baseUrl)

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

//    fun initialize(context: Context) {
//        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
//        baseUrl = sharedPreferences.getString(LAST_USED_URL_KEY, app.HOST_URL) ?: app.HOST_URL
//        rebuildRetrofit()
//    }

    fun setBaseUrl(newUrl: String) {
        if (newUrl.toHttpUrlOrNull() != null) {
            baseUrl = newUrl
            app.HOST_URL = newUrl
//            saveLastUsedUrl(newUrl)
            rebuildRetrofit()
        } else {
            Log.e("RetrofitInstance", "Invalid URL: $newUrl")
        }
    }

//    private fun saveLastUsedUrl(url: String) {
//        with(sharedPreferences.edit()) {
//            putString(LAST_USED_URL_KEY, url)
//            apply()
//        }
//    }

    private fun rebuildRetrofit() {
        retrofit = createRetrofit(baseUrl)
    }

    private fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
    }

    // Imgur-specific Retrofit instance creation
    private fun createImgurRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(IMGUR_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Function to upload image to Imgur
    fun uploadImageToImgur(imageFile: File, callback: (String?) -> Unit) {
        val imgurApi = createImgurRetrofit().create(ApiService::class.java)

        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
        val body = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
        val authHeader = "Client-ID $IMGUR_CLIENT_ID"

        imgurApi.uploadImage(authHeader, body).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: retrofit2.Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.string()
                    val imageUrl = try {
                        val jsonObject = jsonResponse?.let { JSONObject(it) }
                        jsonObject?.getJSONObject("data")?.getString("link")
                    } catch (e: JSONException) {
                        Log.e("ImgurUpload", "JSON Parsing error: ${e.message}")
                        null
                    }
                    callback(imageUrl)
                } else {
                    Log.e("ImgurUpload", "Upload failed: ${response.message()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("ImgurUpload", "Network error: ${t.message}")
                callback(null)
            }
        })
    }
}
