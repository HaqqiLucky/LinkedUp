package com.example.linkedup.utils

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CompanyDao {
    @Query("SELECT * FROM company")
    fun getAll(): LiveData<List<Loker>>

    @Insert
    suspend fun insert(vararg loker: Loker)
}