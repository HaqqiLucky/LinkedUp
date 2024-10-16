package com.example.linkedup.utils

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.Date

@Entity(tableName = "loker")
data class Loker (
    @PrimaryKey(autoGenerate = true) val _id: Int = 0,
    val title: String,
    val gaji: Int,
    val deskripsi: String,
    val instansi: String,
    val dibuat: Date,
    val status: Boolean,
)
class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}