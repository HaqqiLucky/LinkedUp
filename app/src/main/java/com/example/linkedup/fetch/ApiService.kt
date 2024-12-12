package com.example.linkedup.fetch

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthApiService {
    @POST("api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}
interface JobApiService {
    @GET("api/job/search")
    suspend fun searchJobs(
        @Query("title") title: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): JobPagingResponse
    @Multipart
    @POST("api/job")
    fun createJob(
        @Part("title") title: RequestBody,
        @Part("salary") salary: RequestBody,
        @Part("description") description: RequestBody,
        @Part("companyId") companyId: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<Void>
    data class JobUpdateReq(
        val title: String,
        val salary: Int,
        val description: String
    )
    @PUT("api/job/{id}")
    suspend fun updateJob(
        @Path("id") id: String,
        @Body jobUpdatesReq: JobUpdateReq
    ): JobResponse
    @DELETE("api/job/{id}")
    suspend fun deleteJob(
        @Path("id") id: String
    ): Call<ResponseMessage>

    @POST("api/job/registerforjob")
    suspend fun registerForJob(
        @Body RegisterForJobRequest: RegisterForJobRequest
    ): ResponseMessage
    @GET("api/job/getapplicant")
    suspend fun getApplicant(): List<JobUsers>
}

interface CompanyApiService {
    @GET("api/companies/search")
    suspend fun getCompanies(
        @Query("name") name: String,
    ): List<CompanyResponse>
    @POST("api/companies")
    fun createCompanies(@Body company: Company): Call<Void>
    @PUT("api/companies/{id}")
    suspend fun updateCompany(
        @Path("id") id: String,
        @Body company: Company
    ): Company
    @DELETE("api/companies/{id}")
    suspend fun deleteCompany(
        @Path("id") id: String
    ): Call<ResponseMessage>


}

interface UserApiService {
    @POST("api/auth/register")
    suspend fun register(@Body user: RegisterRequest): RegisterResponse
    @GET("api/users/me")
    suspend fun getMe(): User
    @GET("api/users/me")
    fun getCurrentUser(@Header("Authorization") token: String): Call<User>
    @Multipart
    @PUT("api/users/me/image")
    suspend fun submitImage(
        @Part image: MultipartBody.Part
    ): Response<ResponseMessage>
    @PUT("api/users/me")
    suspend fun updateProfile(@Body user: UpdateProfileRequest): Response<User>
    @DELETE("api/users/me")
    suspend fun deleteUser(@Path("id") id: String): Response<ResponseMessage>
    @DELETE("api/users/image")
    suspend fun deleteProfileImage(): Response<ResponseMessage>
    @POST("/api/users/experience")
    suspend fun postExperience(@Body experience: Experience): Response<Experience>
    @GET("/api/users/experience")
    suspend fun getExperiences(): Response<List<Experience>>

    @GET("/api/users/education")
    suspend fun getEducations(): Response<List<Education>>
    @POST("/api/users/education")
    suspend fun addEducation(
        @Body request: EducationRequest
    ): Response<EducationResponse>
    @PUT("education/{id}")
    suspend fun updateEducation(
        @Path("id") id: Int,
        @Body education: Education
    ): Response<Education>
    @DELETE("education/{id}")
    suspend fun deleteEducation(@Path("id") id: Int): Response<Unit>
}

data class ResponseMessage(
    val message: String
)
