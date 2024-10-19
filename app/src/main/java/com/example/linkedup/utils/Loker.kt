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
    val dibuat: String,
    val status: Boolean,
)