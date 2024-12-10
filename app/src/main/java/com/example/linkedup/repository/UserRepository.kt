package com.example.linkedup.repository

import android.util.Log
import com.example.linkedup.fetch.RegisterRequest
import com.example.linkedup.fetch.RegisterResponse
import com.example.linkedup.fetch.RetrofitClient
import com.example.linkedup.fetch.User

class UserRepository {
    private val apiService = RetrofitClient.UserApiServices
    suspend fun register(
        name: String,
        email: String,
        password: String
    ): RegisterResponse? {
        val request = RegisterRequest(name = name, email =  email, password =  password)
        val response = apiService.register(request)
        Log.d("register", response.toString())
        return response
    }
}