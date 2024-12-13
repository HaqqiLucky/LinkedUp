package com.example.linkedup.repository

import android.util.Log
import com.example.linkedup.fetch.Job
import com.example.linkedup.fetch.JobApiService
import com.example.linkedup.fetch.JobPagingResponse
import com.example.linkedup.fetch.JobResponse
import com.example.linkedup.fetch.JobUsers
import com.example.linkedup.fetch.RegisterForJobRequest
import com.example.linkedup.fetch.ResponseMessage
import com.example.linkedup.fetch.RetrofitClient
import okhttp3.MultipartBody
import retrofit2.Response
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.Body
import java.io.File

class JobRepository {

    private val apiService = RetrofitClient.JobApiServices

    suspend fun searchJobs(title: String, page: Int = 1, pageSize: Int = 10): JobPagingResponse {
        try {
            val response = apiService.searchJobs(title, page, pageSize)
            Log.d("JobRepository", "Response from API for search: $response")
            return response
        } catch (e: Exception) {
            Log.e("JobRepository", "Error fetching jobs: ${e.message}")
            throw e
        }
    }

    fun createJob(
        title: RequestBody,
        salary: RequestBody,
        description: RequestBody,
        companyId: RequestBody,
        image: MultipartBody.Part,
        callback: (Result<Void>) -> Unit
    ) {
        // Mengirim request menggunakan enqueue
        val call = apiService.createJob(title, salary, description, companyId, image)

        // Panggil enqueue pada objek Call
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback(Result.success(response.body()) as Result<Void>)
                } else {
                    callback(Result.failure(Exception("Failed to send data")))
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    suspend fun getApplicant(): List<JobUsers> {
        val res = apiService.getApplicant()
        Log.d("get applicant", res.toString())
        return res
    }
    suspend fun acceptApplicant(jobId: String, userId: String): ResponseMessage{
        val res = apiService.acceptApplicant(jobId, userId)
        Log.d("accept applicant", res.toString())
        return res
    }
    suspend fun registerForJob(RegisterForJobRequest: RegisterForJobRequest): ResponseMessage {
        val res = apiService.registerForJob(RegisterForJobRequest)
        Log.d("RegisterForJobRequest", res.toString())
        return res
    }
    suspend fun getJobsForUser(): List<JobResponse> {
        val res = apiService.getJobsForUser()
        Log.d("getJobsForUser", res.toString())
        return res
    }

    suspend fun updateJob(id: String, title: String, salary: Int, description: String): JobResponse {
        val request = JobApiService.JobUpdateReq(title,salary,description)
        val response = apiService.updateJob(id, request)
        Log.d("updatejob", response.toString())
        return response
    }
    suspend fun deleteJob(id: String): Call<ResponseMessage> {
        val response = apiService.deleteJob(id)
        return response
    }

}
