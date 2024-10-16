package com.example.linkedup.utils

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "company")
data class Company (
    @PrimaryKey(autoGenerate = true) val _id: Int = 0,
    val nama: String,
    val alamat: String,
    val web: String
)