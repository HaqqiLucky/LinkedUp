package com.example.linkedup.item

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linkedup.fetch.RetrofitClient
import com.example.linkedup.utils.Experience
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Response
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody

class ExperienceViewModel : ViewModel() {
    private val apiService = RetrofitClient.UserApiServices
    private val _experiences = MutableStateFlow<List<Experience>>(emptyList())
    val experiences: StateFlow<List<Experience>> = _experiences.asStateFlow()
    private var userId: Int = -1
    private var highlightedId: Int? = null

    fun getUserId(): Int {
        Log.d("ExperienceViewModel", "Getting userId: $userId")
        return userId
    }

    fun setUserId(id: Int) {
        Log.d("ExperienceViewModel", "Setting userId to: $id")
        userId = id
        loadExperiences()
    }

    suspend fun addExperience(judulExperience: String, tempatExperience: String, highligted: Boolean): Response<com.example.linkedup.utils.Experience> {
        return try {
            if (userId == -1) {
                Log.e("ExperienceViewModel", "Attempting to add experience without valid userId")
                return Response.error(
                    400,
                    "User ID not set".toResponseBody("text/plain".toMediaType())
                )
            }

            val apiExperience = com.example.linkedup.fetch.Experience(
                jobTitle = judulExperience,
                company = tempatExperience,
                userId = userId.toString()
            )
            
            Log.d("ExperienceViewModel", "Sending experience with userId: $userId")
            val response = apiService.postExperience(apiExperience)
            
            if (response.isSuccessful) {
                response.body()?.let { apiExp ->
                    if (highligted) {
                        apiExp._id?.toIntOrNull()?.let { newId ->
                            highlightedId = newId
                        }
                    }
                    loadExperiences()
                    return Response.success(Experience(
                        _id = apiExp._id?.toIntOrNull() ?: 0,
                        judulExperience = apiExp.jobTitle,
                        tempatExperience = apiExp.company,
                        highligted = highligted
                    ))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("ExperienceViewModel", "Error adding experience: $errorBody")
                Log.e("ExperienceViewModel", "UserId when error: $userId")
            }
            Response.error(
                response.code(),
                response.errorBody() ?: "Unknown error".toResponseBody("text/plain".toMediaType())
            )
        } catch (e: Exception) {
            Log.e("ExperienceViewModel", "Exception adding experience", e)
            Response.error(
                500,
                e.message?.toResponseBody("text/plain".toMediaType()) 
                    ?: "Unknown error".toResponseBody("text/plain".toMediaType())
            )
        }
    }

    private fun loadExperiences() {
        viewModelScope.launch {
            try {
                val response = apiService.getExperiences()
                if (response.isSuccessful) {
                    response.body()?.let { fetchedExperiences ->
                        val convertedExperiences = fetchedExperiences.map { fetchExp ->
                            Experience(
                                _id = fetchExp._id?.toIntOrNull() ?: 0,
                                judulExperience = fetchExp.jobTitle,
                                tempatExperience = fetchExp.company,
                                highligted = fetchExp._id?.toIntOrNull() == highlightedId
                            )
                        }.sortedByDescending { it.highligted }
                        _experiences.value = convertedExperiences
                    }
                }
            } catch (e: Exception) {
                Log.e("ExperienceViewModel", "Load Error: ${e.message}")
            }
        }
    }
}