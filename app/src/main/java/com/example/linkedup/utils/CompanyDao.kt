package com.example.linkedup.utils

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CompanyDao {
    @Query("SELECT * FROM company")
    fun getAll(): List<Company>

    @Insert
    suspend fun insert(vararg company: Company)

    @Delete
    suspend fun delete(vararg company: Company)

    @Update
    suspend fun update(vararg company: Company)
}