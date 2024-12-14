package com.example.linkedup.utils

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "educations")
data class Education(
    @PrimaryKey(autoGenerate = true)
    val _id: Int = 0,
    val degree: String,
    val schoolName: String
) 