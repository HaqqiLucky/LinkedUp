package com.example.linkedup.fetch

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.linkedup.LinkedUp
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val BASE_URL = ConfigManager.getBaseUrl()

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val newRequest = originalRequest.newBuilder()
                .apply {
                    val token = AuthPrefs.getToken()
                    if (token.isNotEmpty()) {
                        addHeader("Authorization", "Bearer $token")
                    }
                    val apiKey = ConfigManager.getApiKey()
                    if (apiKey.isNotEmpty()) {
                        addHeader("x-api-key", apiKey)
                    }
                }
                .build()

            chain.proceed(newRequest)
        }
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val JobApiServices: JobApiService by lazy {
        retrofit.create(JobApiService::class.java)
    }

    val UserApiServices: UserApiService by lazy {
        retrofit.create(UserApiService::class.java)
    }

    val CompanyApiServices: CompanyApiService by lazy {
        retrofit.create(CompanyApiService::class.java)
    }

    val authApiService: AuthApiService = retrofit.create(AuthApiService::class.java)
}
object ConfigManager {
    private const val BASE_URL = "https://linkedup.muazmhafidz.my.id/"
    private const val apikey = "muaztamvansekali"
    fun getBaseUrl(): String {
        return BASE_URL
    }

    fun getApiKey(): String {
        return apikey
    }

    fun isConnectedToInternet(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork

        if (activeNetwork != null) {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
            return networkCapabilities != null && (networkCapabilities.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        }
        return false
    }
}

object AuthPrefs {
    private const val PREF_NAME = "app_prefs"
    private const val TOKEN_KEY = "token_key"

    fun saveToken(token: String) {
        val sharedPref = LinkedUp.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPref.edit().putString(TOKEN_KEY, token).apply()
    }
    fun getToken(): String {
        val sharedPref = LinkedUp.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(TOKEN_KEY, "") ?: ""
    }
    fun clearToken() {
        val sharedPref = LinkedUp.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPref.edit().remove(TOKEN_KEY).apply()
    }
}

