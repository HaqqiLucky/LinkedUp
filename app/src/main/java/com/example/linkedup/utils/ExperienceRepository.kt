package com.example.linkedup.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ExperienceRepository(private val experienceDao: ExperienceDao) {

//    val allExperiences : LiveData<List<com.example.linkedup.utils.Experience>> = experienceDao.getAllExperiences()

//    val allUsers: Flow<List<Experience>> = experienceDao.getAllUsers()

    suspend fun insert(experience: Experience) {
        return withContext(Dispatchers.IO) {
            experienceDao.insert(experience)
        }
    }

    suspend fun update(experience:Experience){
        experienceDao.update(experience)
    }

    suspend fun delete(experience: Experience){
        experienceDao.delete(experience)
    }
    suspend fun getAllExperience(): List<Experience>{
        return experienceDao.getAllExperience()
    }
}