package com.example.linkedup

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "loker")
data class Loker (
    @PrimaryKey(autoGenerate = true) val _id: Int,
    val title: String,
    val gaji: Int,
    val deskripsi: String,
    val instansi: String,
    val dibuat: Date,
    val status: Boolean,
)