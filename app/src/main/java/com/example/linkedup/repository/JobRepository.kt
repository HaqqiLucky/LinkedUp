package com.example.linkedup.repository

import android.util.Log
import com.example.linkedup.fetch.Job
import com.example.linkedup.fetch.JobApiService
import com.example.linkedup.fetch.JobPagingResponse
import com.example.linkedup.fetch.ResponseMessage
import com.example.linkedup.fetch.RetrofitClient
import okhttp3.MultipartBody
import retrofit2.Response
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
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

//    suspend fun createJob(
//        title: String,
//        salary: Int,
//        description: String,
//        companyId: Int,
//        imageFile: File
//    ): Job? {
//        val titleBody = RequestBody.create("text/plain".toMediaType(), title)
//        val salaryBody = RequestBody.create("text/plain".toMediaType(), salary.toString())
//        val descriptionBody = RequestBody.create("text/plain".toMediaType(), description)
//        val companyIdBody = RequestBody.create("text/plain".toMediaType(), companyId.toString())
//
//        val imagePart = MultipartBody.Part.createFormData(
//            "image", imageFile.name,
//            RequestBody.create("image/jpeg".toMediaType(), imageFile)
//        )
//
//        val response = apiService.createJob(titleBody, salaryBody, descriptionBody, companyIdBody, imagePart)
//        Log.d("createjob", response.toString())
//        return response
//    }

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

    suspend fun updateJob(id: Int, title: String, salary: Int, description: String): Job {
        val request = JobApiService.JobUpdateReq(title,salary,description)
        val response = apiService.updateJob(id, request)
        Log.d("updatejob", response.toString())
        return response
    }
    suspend fun deleteJob(id: Int): Call<ResponseMessage> {
        val response = apiService.deleteJob(id)
        return response
    }

}
