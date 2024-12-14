package com.example.linkedup.item

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linkedup.fetch.RetrofitClient
import com.example.linkedup.utils.Experience
import com.example.linkedup.utils.ExperienceRepository
import com.example.linkedup.utils.LokerDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Response
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody

class ExperienceViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ExperienceRepository
    private val _experiences = MutableStateFlow<List<Experience>>(emptyList())
    val experiences: StateFlow<List<Experience>> = _experiences.asStateFlow()

    init {
        val database = LokerDatabase.getDatabase(application)
        repository = ExperienceRepository(database.experienceDao())
        
        viewModelScope.launch {
            repository.allExperiences.collect { experiences ->
                _experiences.value = experiences
            }
        }
    }

    fun addExperience(title: String, company: String, isHighlighted: Boolean) {
        viewModelScope.launch {
            if (isHighlighted) {
                // Remove highlight from other experiences
                _experiences.value.find { it.isHighlighted }?.let { oldHighlighted ->
                    repository.update(oldHighlighted.copy(isHighlighted = false))
                }
            }
            repository.insert(Experience(title = title, company = company, isHighlighted = isHighlighted))
        }
    }

    fun updateExperience(experience: Experience) {
        viewModelScope.launch {
            if (experience.isHighlighted) {
                // Remove highlight from other experiences
                _experiences.value.find { it.isHighlighted && it._id != experience._id }?.let { oldHighlighted ->
                    repository.update(oldHighlighted.copy(isHighlighted = false))
                }
            }
            repository.update(experience)
        }
    }

    fun deleteExperience(experience: Experience) {
        viewModelScope.launch {
            repository.delete(experience)
        }
    }
}