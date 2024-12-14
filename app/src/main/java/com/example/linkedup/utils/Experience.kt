package com.example.linkedup.utils

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "experiences")
data class Experience(
    @PrimaryKey(autoGenerate = true)
    val _id: Int = 0,
    val title: String,
    val company: String,
    val isHighlighted: Boolean = false
)
