//package com.example.linkedup.item
//
//import android.app.Application
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.viewModelScope
//import com.example.linkedup.utils.Experience
//import com.example.linkedup.utils.ExperienceRepository
//import com.example.linkedup.utils.LokerDatabase
//import kotlinx.coroutines.launch
//
//class ExperienceViewModel (application: Application) : AndroidViewModel(application){
//
//    private val repository: ExperienceRepository
//    private val _allExperiences = MutableLiveData<List<Experience>>()
//    val allExperience :LiveData<List<Experience>> get() = _allExperiences
//    init {
//        val experienceDao = LokerDatabase.getDatabase(application).experienceDao()
//        repository = ExperienceRepository(experienceDao)
//        fetchAllexperience()
//    }
//
//    private fun fetchAllexperience(){
//        viewModelScope.launch {
//            _allExperiences.value = repository.getAllExperience()
//        }
//    }
//
//    fun insert(experience: Experience) = viewModelScope.launch{
//        repository.insert(experience)
//        fetchAllexperience()
//    }
//
//    fun update(experience: Experience) = viewModelScope.launch{
//        repository.update(experience)
//        fetchAllexperience()
//    }
//
//    fun delete(experience: com.example.linkedup.item.Experience) = viewModelScope.launch{
//        repository.delete(experience)
//        fetchAllexperience()
//    }
//
//
//
//}