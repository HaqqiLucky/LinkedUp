package com.example.linkedup.item

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.linkedup.fetch.Job
import com.example.linkedup.repository.JobRepository
import com.example.linkedup.fetch.Company
import com.example.linkedup.fetch.CompanyResponse
import com.example.linkedup.fetch.JobResponse
import com.example.linkedup.fetch.JobUsers
import com.example.linkedup.fetch.ResponseMessage
import com.example.linkedup.repository.CompanyRepository
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val jobRepository = JobRepository()
    private val companyRepository = CompanyRepository()

    private val _jobsLiveData = MutableLiveData<List<JobResponse>>()
    val jobsLiveData: LiveData<List<JobResponse>> get() = _jobsLiveData

    private val _companiesLiveData = MutableLiveData<List<CompanyResponse>>()
    val companiesLiveData: LiveData<List<CompanyResponse>> get() = _companiesLiveData

    private val _apllyLiveData = MutableLiveData<List<JobUsers>>()
    val applyLiveData: LiveData<List<JobUsers>> get() = _apllyLiveData

    private val _historyLiveData = MutableLiveData<List<JobResponse>>()
    val historyLiveData: LiveData<List<JobResponse>> get() = _historyLiveData


    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading
//    private var tiga_puluh: Boolean = true


    init {
        fetchAllLoker()
        fetchAllCompany()
        fetchAllApply()
        getJobsForUser()
    }
    private fun fetchAllApply() {
        viewModelScope.launch {
            try {
                val response = jobRepository.getApplicant()
                _apllyLiveData.postValue(response)
            } catch (e: Exception) {
                _apllyLiveData.postValue(emptyList())
            }
        }
    }
    private fun getJobsForUser() {
        viewModelScope.launch {
            try {
                val response = jobRepository.getJobsForUser()
                _historyLiveData.postValue(response)
            } catch (e: Exception) {
                _historyLiveData.postValue(emptyList())
            }
        }
    }
    fun acceptApplicant(jobId: String, userId: String) {
        viewModelScope.launch {
            jobRepository.acceptApplicant(jobId, userId)
        }
    }
    fun fetchAllLoker() {
        viewModelScope.launch {
            try {
                val response = jobRepository.searchJobs("")
                _jobsLiveData.postValue(response.jobs)
//                tiga_puluh = true
            } catch (e: Exception) {
                _jobsLiveData.postValue(emptyList())
            }
        }
    }

    // job
//    fun fetchAllLokerOther() {
//        viewModelScope.launch {
//            try {
//                if (tiga_puluh) {
//                    val response = jobRepository.searchJobs("", pageSize = 30)
//                    _jobsLiveData.postValue(response.jobs)
//                    tiga_puluh = false
//                } else {}
//            } catch (e: Exception) {
//                _jobsLiveData.postValue(emptyList())
//            }
//        }
//    }
    fun searchJobs(title: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = jobRepository.searchJobs(title)
                _jobsLiveData.postValue(response.jobs)
            } catch (e: Exception) {
                Log.e("JobViewModel", "Error: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }
    fun createJob(title: String, salary: Int, description: String, companyId: String, imageFile: File) {
            val titleBody = RequestBody.create("text/plain".toMediaType(), title)
            val salaryBody = RequestBody.create("text/plain".toMediaType(), salary.toString())
            val descriptionBody = RequestBody.create("text/plain".toMediaType(), description)
            val companyIdBody = RequestBody.create("text/plain".toMediaType(), companyId.toString())

            val imagePart = MultipartBody.Part.createFormData(
                "image", imageFile.name,
                RequestBody.create("image/jpeg".toMediaType(), imageFile)
            )
                try {
                    jobRepository.createJob(titleBody, salaryBody, descriptionBody, companyIdBody, imagePart) { result ->
                            Log.d("create job", "result: "+result.toString())
                        }
                } catch (e: Exception) {
                    Log.d("create job", "Error: ${e.message}")
                }
    }
    suspend fun updateJob(id: String, title: String, salary: Int, description: String) {
        try {
            val response = jobRepository.updateJob(id, title, salary, description)
            Log.d("update job", "success:"+response.toString())
        } catch (e: Exception) {
            Log.d("update job", "Failed to put job: "+e.message)
        }
    }
    suspend fun deleteJob(id: String) {
        try {
            val response = jobRepository.deleteJob(id)
            Log.d("delete job", "success:"+response.toString())
        } catch (e: Exception) {
            Log.d("delete job", "Failed to del job: "+e.message)
        }
    }

    //company
    fun fetchAllCompany(name: String = "") {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = companyRepository.getCompanies(name)
                _companiesLiveData.postValue(response)
            } catch (e: Exception) {
                Log.e("CompanyViewModel", "Error: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }
    fun createCompany(company: Company) {
        companyRepository.createCompanies(company) { result ->
            Log.d("create company", "result: "+result.toString())
        }
    }
    suspend fun updateCompany(id: String, company: Company) {
        try {
            val response = companyRepository.updateCompany(id, company)
            Log.d("update company", "success:"+response.toString())
        } catch (e: Exception) {
            Log.d("update company", "Failed to put company: "+e.message)
        }
    }
    suspend fun deleteCompany(id: String) {
        try {
            val response = companyRepository.deleteCompany(id)
            Log.d("delete comp", "success:"+response.toString())
        } catch (e: Exception) {
            Log.d("delete comp", "Failed to del comp: "+e.message)
        }
    }


//    fun insert(loker: Loker) = viewModelScope.launch {
//        repository.insert(loker)
//        fetchAllLoker()
//    }
//    fun delete(loker: Loker) = viewModelScope.launch {
//        repository.delete(loker)
//        fetchAllLoker()
//    }
//    fun update(loker: Loker) = viewModelScope.launch {
//        repository.update(loker)
//        fetchAllLoker()
//    }
//
//    private fun fetchAllCompany() {
//        viewModelScope.launch {
//            _allCompany.value = repository2.getAllCompany()
//        }
//    }
//
//    fun insert(company: Company) = viewModelScope.launch {
//        repository2.insert(company)
//        fetchAllCompany()
//    }
//
//    fun delete(company: Company) = viewModelScope.launch {
//        repository2.delete(company)
//        fetchAllCompany()
//    }
//
//    fun update(company: Company) = viewModelScope.launch {
//        repository2.update(company)
//        fetchAllCompany()
//    }
//
//    fun fetchAllCompanyNames() {
//        viewModelScope.launch {
//            val names = repository2.getAllCompanyNames()
//            _companyNames.value = names
//        }
//    }
}