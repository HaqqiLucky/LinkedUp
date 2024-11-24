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
        @Path("id") id: Int,
        @Body jobUpdatesReq: JobUpdateReq
    ): Job
    @DELETE("api/job/{id}")
    suspend fun deleteJob(
        @Path("id") id: Int
    ): Call<ResponseMessage>
}

interface CompanyApiService {
    @GET("api/companies/search")
    suspend fun getCompanies(
        @Query("name") name: String,
    ): List<Company>
    @POST("api/companies")
    fun createCompanies(@Body company: Company): Call<Void>
    @PUT("api/companies/{id}")
    suspend fun updateCompany(
        @Path("id") id: Int,
        @Body company: Company
    ): Company
    @DELETE("api/companies/{id}")
    suspend fun deleteCompany(
        @Path("id") id: Int
    ): Call<ResponseMessage>
}

interface UserApiService {
    @GET("api/users/me")
    suspend fun getMe(@Header("Authorization") token: String): User
    @GET("api/users/me")
    fun getCurrentUser(@Header("Authorization") token: String): Call<User>
}

data class ResponseMessage(
    val message: String
)