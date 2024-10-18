package com.example.linkedup.utils

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class, Company::class, Loker::class], version = 1)
abstract class LokerDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun lokerDao(): LokerDao
    abstract fun companyDao(): CompanyDao

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