package com.example.linkedup.utils

import androidx.lifecycle.LiveData

class LokerRepository(private val lokerDao: LokerDao) {

    val allLoker: LiveData<List<Loker>> = lokerDao.getAll()

    suspend fun insert(loker: Loker) {
        lokerDao.insert(loker)
    }
}