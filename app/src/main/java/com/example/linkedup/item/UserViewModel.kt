package com.example.linkedup.item

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import com.example.linkedup.fetch.ResponseMessage
import com.example.linkedup.fetch.RetrofitClient
import com.example.linkedup.fetch.UpdateProfileRequest
import com.example.linkedup.fetch.User
import retrofit2.Response

class UserViewModel(application: Application): AndroidViewModel(application) {

    suspend fun getProfile(): User {
        return RetrofitClient.UserApiServices.getMe()
    }

    suspend fun updateProfile(updateRequest: UpdateProfileRequest): Response<User> {
        return RetrofitClient.UserApiServices.updateProfile(updateRequest)
    }

    suspend fun deleteUserAccount(userId: String): Response<ResponseMessage> {
        return RetrofitClient.UserApiServices.deleteUser(userId.toString())
    }
}

