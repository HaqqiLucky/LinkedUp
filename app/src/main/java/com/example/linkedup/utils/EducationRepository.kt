package com.example.linkedup.utils

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class EducationRepository(private val educationDao: EducationDao) {
    val allEducations: Flow<List<Education>> = educationDao.getAllEducations()

    @WorkerThread
    suspend fun insert(education: Education) {
        educationDao.insert(education)
    }

    @WorkerThread
    suspend fun update(education: Education) {
        educationDao.update(education)
    }

    @WorkerThread
    suspend fun delete(education: Education) {
        educationDao.delete(education)
    }
} 