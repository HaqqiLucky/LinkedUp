package com.example.linkedup.utils

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="experiences")
data class Experience(
    val _id: Int = 0,
    val judulExperience: String,
    val tempatExperience: String,
    val highligted: Boolean
)
