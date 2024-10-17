package com.example.linkedup.utils

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LokerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(loker: Loker)

    @Query("SELECT * FROM loker ORDER BY _id DESC")
    suspend fun getAllLoker(): List<Loker>

//    @Delete
//    suspend fun delete(loker: Loker)
}