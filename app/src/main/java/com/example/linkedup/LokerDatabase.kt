package com.example.linkedup

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class LokerDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
        companion object {
            @Volatile
            private var INSTANCE: LokerDatabase? = null
            fun getInstance(context: Context): LokerDatabase {
                return INSTANCE ?: synchronized(this) {
                    INSTANCE ?: Room.databaseBuilder(
                        context.applicationContext,
                        LokerDatabase::class.java, "loker_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                        .also { INSTANCE = it }
                }

            }
        }
}