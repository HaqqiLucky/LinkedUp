package com.example.linkedup.utils

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LokerDao {
    @Query("SELECT * FROM loker ORDER BY dibuat DESC")
    fun getAll(): LiveData<List<Loker>>

    @Insert
    fun insert(vararg loker: Loker)
}