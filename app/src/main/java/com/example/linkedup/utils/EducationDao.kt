package com.example.linkedup.utils

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EducationDao {
    @Query("SELECT * FROM educations ORDER BY degree ASC")
    fun getAllEducations(): Flow<List<Education>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(education: Education)

    @Update
    suspend fun update(education: Education)

    @Delete
    suspend fun delete(education: Education)

    @Query("DELETE FROM educations")
    suspend fun deleteAll()
} 