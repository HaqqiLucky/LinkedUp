package com.example.linkedup.item

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.linkedup.utils.EducationRepository
import com.example.linkedup.utils.Education
import com.example.linkedup.utils.LokerDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EducationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: EducationRepository
    private val _educations = MutableStateFlow<List<Education>>(emptyList())
    val educations: StateFlow<List<Education>> = _educations

    init {
        val database = LokerDatabase.getDatabase(application)
        repository = EducationRepository(database.educationDao())
        
        viewModelScope.launch {
            repository.allEducations.collect { educations ->
                _educations.value = educations
            }
        }
    }

    fun addEducation(degree: String, schoolName: String) {
        viewModelScope.launch {
            repository.insert(Education(degree = degree, schoolName = schoolName))
        }
    }

    fun updateEducation(education: Education) {
        viewModelScope.launch {
            repository.update(education)
        }
    }

    fun deleteEducation(education: Education) {
        viewModelScope.launch {
            repository.delete(education)
        }
    }
} 