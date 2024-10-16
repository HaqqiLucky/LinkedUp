package com.example.linkedup.utils

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LokerDao {
    @Query("SELECT * FROM loker")
    fun getAll(): LiveData<List<Loker>>

    @Insert
    suspend fun insert(vararg loker: Loker)
}