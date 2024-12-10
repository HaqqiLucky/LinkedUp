package com.example.linkedup.repository

import android.util.Log
import com.example.linkedup.fetch.Company
import com.example.linkedup.fetch.CompanyResponse
import com.example.linkedup.fetch.ResponseMessage
import com.example.linkedup.fetch.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompanyRepository {
    private val apiService = RetrofitClient.CompanyApiServices

    suspend fun getCompanies(name: String): List<CompanyResponse> {
        try {
            val response = apiService.getCompanies(name)
            Log.d("CompanyRepository", "Response from API for search: $response")
            return response
        } catch (e: Exception) {
            Log.e("JobRepository", "Error fetching jobs: ${e.message}")
            throw e
        }
    }

    fun createCompanies(company: Company, callback: (Result<Void>) -> Unit) {
        apiService.createCompanies(company).enqueue(object : Callback<Void> {
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

    suspend fun updateCompany(_id: String, company: Company): Company {
        val response = apiService.updateCompany(_id, company)
        Log.d("updatecomp", response.toString())
        return response
    }

    suspend fun deleteCompany(id: String): Call<ResponseMessage> {
        val response = apiService.deleteCompany(id)
        return response
    }

}