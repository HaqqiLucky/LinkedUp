package com.example.linkedup.utils

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import org.junit.Test

@Database(entities = [JobEntity::class, Education::class, Experience::class], version = 1)
abstract class LokerDatabase : RoomDatabase() {
    abstract fun jobDao(): JobDao
    abstract fun educationDao(): EducationDao
    abstract fun experienceDao(): ExperienceDao

    companion object {
            @Volatile
            private var INSTANCE: LokerDatabase? = null
            fun getDatabase(context: Context): LokerDatabase {
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        LokerDatabase::class.java,
                        "loker_database"
                    ).build()
                    INSTANCE = instance
                    instance
                }
            }
        }
}

@Entity(tableName = "jobs")
data class JobEntity(
    @PrimaryKey val id: String,
    val title: String,
    val salary: Int,
    val description: String,
    val createdAt: String,
    val status: Boolean,
    val image: String,
    val company: String,
)

@Dao
interface JobDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJob(job: JobEntity)

    @Delete
    suspend fun delete(job: JobEntity)

    @Query("SELECT * FROM jobs")
    suspend fun getAllJobs(): List<JobEntity>

    @Query("DELETE FROM jobs")
    suspend fun deleteAllJobs()
}