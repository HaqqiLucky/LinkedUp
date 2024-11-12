package com.example.linkedup.utils

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExperienceDao {

    @Query("SELECT * FROM experiences ORDER BY judulExperience ASC")
    fun getAllExperience(): List<Experience>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg experience: Experience)

    @Delete
    suspend fun delete(experience: Experience)

    @Update
    suspend fun update(experience: Experience)

}