package com.example.linkedup.fetch

import com.google.gson.annotations.SerializedName
import java.util.Date
data class LoginRequest(
    val email: String,
    val password: String
)
data class LoginResponse(
    val token: String
)
data class User(
    val id: Int?,
    val name: String,
    val address: String?,
    val phone: String?,
    val email: String,
    val gender: String?,
    val role: String = "user", // Default role
    val image: String?,
    val companyId: Int?,
    val password: String,
    val experience: List<Experience> = emptyList(), // Relasi Experience
    val education: List<Education> = emptyList(),   // Relasi Education
    val jobs: List<Job> = emptyList()               // Relasi Job
)
data class Job(
    val id: Int?,
    val title: String,
    val salary: Int,
    val description: String,
    @SerializedName("createdAt")
    val createdAt: Date,
    val status: Boolean = true,
    val image: String,
    val userCount: Int = 0,
    val users: List<User> = emptyList(),
    val companyId: Int?,
    val company: Company?
)
data class Company(
    val id: Int?,
    val name: String,
    val address: String,
    val website: String?,
    val users: List<User> = emptyList(),
    val jobs: List<Job> = emptyList()
)
data class JobPagingResponse(
    val jobs: List<Job>,
    val nextCursor: Int?
)
data class Experience(
    val id: Int?,
    val jobTitle: String,
    val company: String,
    val userId: Int
)
data class JobUsers(
    val id: Int?,
    val jobId: Int,
    val userId: Int,
    val portfolioLink: String?,
    val description: String,
    val job: Job,
    val user: User
)
data class Education(
    val id: Int?,
    val degree: String,
    val userId: Int
)
