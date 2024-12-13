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
    val _id: String?,
    val name: String,
    val address: String?,
    val phone: String?,
    val email: String,
    val gender: String?,
    val description: String?,
    val role: String = "user", // Default role
    val image: String?,
    val company_id: String?,
    val password: String,
    val experience: List<Experience> = emptyList(), // Relasi Experience
    val education: List<Education> = emptyList(),   // Relasi Education
    val jobs: List<Job> = emptyList()               // Relasi Job
)
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)
data class RegisterResponse(
    val _id: String,
    val name: String,
    val email: String
)
data class Job(
    val _id: String?,
    val title: String,
    val salary: Int,
    val description: String,
    @SerializedName("createdAt")
    val createdAt: Date,
    val status: Boolean = true,
    val image: String,
    val userCount: Int = 0,
    val users: List<User> = emptyList(),
    val company_id: String?,
    val company: Company?
)
data class JobResponse(
    val _id: String,
    val title: String,
    val salary: Int,
    val description: String,
    @SerializedName("createdAt")
    val createdAt: Date,
    val status: Boolean = true,
    val image: String,
    val userCount: Int = 0,
    val users: List<User> = emptyList(),
    @SerializedName("companyId")
    val company: Company?
)
data class Company(
    val _id: String?,
    val name: String,
    val address: String,
    val website: String?,
    val users: List<User> = emptyList(),
    val jobs: List<Job> = emptyList()
)
data class CompanyResponse(
    val _id: String,
    val name: String,
    val address: String,
    val website: String,
    val users: List<User> = emptyList(),
    val jobs: List<Job> = emptyList()
)
data class JobPagingResponse(
    val jobs: List<JobResponse>,
    val nextCursor: String?
)
data class Experience(
    val _id: String? = null,
    val jobTitle: String,
    val company: String,
    val userId: String? = null  // Diperlukan untuk POST request
)
data class JobUsers(
    val _id: String,
    val job_id: String,
    val user_id: String,
    val portfolioLink: String,
    val description: String,
    @SerializedName("jobId")
    val job: Job,
    @SerializedName("userId")
    val user: User
)
data class RegisterForJobRequest(
    val jobId: String,         // ID of the job being applied to
    val description: String,   // A description from the user (e.g., cover letter or reason for applying)
    val portfolioLink: String? // An optional link to the user's portfolio
)
data class Education(
    val _id: String?,
    val degree: String,
    val user_id: String
)
data class EducationRequest(
    val degree: String
)

data class EducationResponse(
    val _id: String,
    val degree: String,
    val userId: String
)
//tambahan
data class UpdateProfileRequest(
    val name: String,
    val address: String?,
    val description: String?,
    val gender: String?
)
