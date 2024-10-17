package com.example.linkedup.utils

import androidx.lifecycle.LiveData

class LokerRepository(private val lokerDao: LokerDao) {

    suspend fun insert(loker: Loker) {
        lokerDao.insert(loker)
    }

    suspend fun getAllLoker(): List<Loker> {
        return lokerDao.getAllLoker()
    }
}