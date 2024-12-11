package com.example.linkedup.utils

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "educations")
data class Education(
    val _id: Int,
    val degree: String
) 