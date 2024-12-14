package com.example.linkedup.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ExperienceRepository(private val experienceDao: ExperienceDao) {

    val allExperiences: Flow<List<Experience>> = experienceDao.getAllExperiences()

    suspend fun insert(experience: Experience) {
        experienceDao.insert(experience)
    }

    suspend fun update(experience: Experience) {
        experienceDao.update(experience)
    }

    suspend fun delete(experience: Experience) {
        experienceDao.delete(experience)
    }
}