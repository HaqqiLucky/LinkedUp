package com.example.linkedup

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LokerDao {
    @Query("SELECT * FROM loker")
    fun getAll(): LiveData<Loker>

    @Insert
    fun insert(vararg loker: Loker)
}