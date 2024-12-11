package com.example.linkedup.repository

import com.example.linkedup.fetch.RegisterRequest
import com.example.linkedup.fetch.RetrofitClient
import com.example.linkedup.fetch.UpdateProfileRequest
import com.example.linkedup.fetch.User
import retrofit2.Response

class UserRepository {
    suspend fun register(name: String, email: String, password: String) {
        val registerRequest = RegisterRequest(name, email, password)
        RetrofitClient.UserApiServices.register(registerRequest)
    }

    suspend fun updateProfile(updateRequest: UpdateProfileRequest): Response<User> {
        return RetrofitClient.UserApiServices.updateProfile(updateRequest)
    }

    suspend fun getMe(): User {
        return RetrofitClient.UserApiServices.getMe()
    }
}