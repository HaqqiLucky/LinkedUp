package com.example.linkedup.utils

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LokerRepository(private val lokerDao: LokerDao) {

    suspend fun insert(loker: Loker) {
        return withContext(Dispatchers.IO) {
            lokerDao.insert(loker)
        }
    }

    suspend fun delete(loker: Loker) {
        lokerDao.delete(loker)
    }

    suspend fun update(loker: Loker) {
        lokerDao.update(loker)
    }

    suspend fun getAllLoker(): List<Loker> {
        return lokerDao.getAllLoker()
    }

}