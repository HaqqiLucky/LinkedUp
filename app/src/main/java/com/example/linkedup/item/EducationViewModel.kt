package com.example.linkedup.item

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linkedup.fetch.RetrofitClient
import com.example.linkedup.repository.UserRepository
import com.example.linkedup.utils.Education
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class EducationViewModel : ViewModel() {
    private val apiService = RetrofitClient.UserApiServices
    val userRepo = UserRepository()
    private val _educations = MutableStateFlow<List<Education>>(emptyList())
    val educations: StateFlow<List<Education>> = _educations
    private var userId: Int = -1

    fun setUserId(id: Int) {
        userId = id
        loadEducations()
    }

    fun addEducation(degree: String) {
        viewModelScope.launch {
            val result = userRepo.addEducation(degree)
            Log.d("edu", result.toString())
        }
    }

    suspend fun updateEducation(educationId: Int, degree: String): Response<com.example.linkedup.utils.Education> {
        return try {
            val response = apiService.updateEducation(
                educationId,
                com.example.linkedup.fetch.Education(
                    _id = educationId.toString(),
                    degree = degree,
                    user_id = userId.toString()
                )
            )
            if (response.isSuccessful) {
                loadEducations()
                Response.success(com.example.linkedup.utils.Education(
                    _id = response.body()?._id?.toIntOrNull() ?: 0,
                    degree = response.body()?.degree ?: ""
                ))
            } else {
                Response.error(
                    response.code(),
                    response.errorBody() ?: "Unknown error".toResponseBody("text/plain".toMediaType())
                )
            }
        } catch (e: Exception) {
            Log.e("EducationViewModel", "Error updating education", e)
            Response.error(
                500,
                e.message?.toResponseBody("text/plain".toMediaType())
                    ?: "Unknown error".toResponseBody("text/plain".toMediaType())
            )
        }
    }

    private fun loadEducations() {
        viewModelScope.launch {
            try {
                val response = apiService.getEducations()
                if (response.isSuccessful) {
                    response.body()?.let { fetchedEducations ->
                        _educations.value = fetchedEducations.map { fetchEdu ->
                            Education(
                                _id = fetchEdu._id?.toIntOrNull() ?: 0,
                                degree = fetchEdu.degree
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("EducationViewModel", "Load Error: ${e.message}")
            }
        }
    }

    suspend fun deleteEducation(educationId: Int): Response<Unit> {
        return try {
            val response = apiService.deleteEducation(educationId)
            if (response.isSuccessful) {
                loadEducations()
            }
            response
        } catch (e: Exception) {
            Log.e("EducationViewModel", "Error deleting education", e)
            Response.error(
                500,
                e.message?.toResponseBody("text/plain".toMediaType())
                    ?: "Unknown error".toResponseBody("text/plain".toMediaType())
            )
        }
    }
} 