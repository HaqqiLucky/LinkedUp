package com.example.linkedup.repository

import com.example.linkedup.fetch.AuthPrefs
import com.example.linkedup.fetch.Education
import com.example.linkedup.fetch.EducationRequest
import com.example.linkedup.fetch.EducationResponse
import com.example.linkedup.fetch.RegisterRequest
import com.example.linkedup.fetch.RetrofitClient
import com.example.linkedup.fetch.UpdateProfileRequest
import com.example.linkedup.fetch.User
import retrofit2.HttpException
import retrofit2.Response

class UserRepository {
    suspend fun register(name: String, email: String, password: String) {
        val registerRequest = RegisterRequest(name, email, password)
        RetrofitClient.UserApiServices.register(registerRequest)
    }

    suspend fun updateProfile(updateRequest: UpdateProfileRequest): Response<User> {
        return RetrofitClient.UserApiServices.updateProfile(updateRequest)
    }

    suspend fun addEducation(degree: String): Result<EducationResponse> {
        return try {
            val response = RetrofitClient.UserApiServices.addEducation(EducationRequest(degree))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMe(): User {
        return RetrofitClient.UserApiServices.getMe()
    }
}