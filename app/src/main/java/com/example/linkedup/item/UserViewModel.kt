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

    suspend fun updateProfile(updateRequest: UpdateProfileRequest): Response<User> {
        return RetrofitClient.UserApiServices.updateProfile(updateRequest)
    }

    suspend fun deleteUserAccount(userId: Int): Response<ResponseMessage> {
        return RetrofitClient.UserApiServices.deleteUser(userId.toString())
    }

    // Fungsi untuk menyimpan user ke shared preferences
    private fun saveUserToPreferences(user: User) {
        val sharedPref = getApplication<Application>().getSharedPreferences("user_prefs", AppCompatActivity.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("user_id", user._id)
            putString("user_name", user.name)
            putString("user_address", user.address)
            putString("user_email", user.email)
            putString("user_description", user.description)
            putString("user_gender", user.gender)
            putString("user_image", user.image)
            apply()
        }
    }
}

