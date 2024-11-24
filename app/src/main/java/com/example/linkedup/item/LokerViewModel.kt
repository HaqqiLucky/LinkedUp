package com.example.linkedup.item

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.linkedup.fetch.Job
import com.example.linkedup.repository.JobRepository
import com.example.linkedup.utils.Loker
import com.example.linkedup.utils.LokerDatabase
import com.example.linkedup.utils.LokerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LokerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LokerRepository
    private val repository2 = JobRepository()
    private val _allLoker = MutableLiveData<List<Job>?>()
    val allLoker: MutableLiveData<List<Job>?> get() = _allLoker
    init {
        val lokerDao = LokerDatabase.getDatabase(application).lokerDao()
        repository = LokerRepository(lokerDao)
        fetchAllLoker()
    }
    private fun fetchAllLoker() = liveData(Dispatchers.IO)  {
//        viewModelScope.launch {
//            _allLoker.value = repository.getAllLoker()
//        }
        try {

//            val jobs = repository2.getJobs()

//            emit(jobs)

//            _allLoker.postValue(jobs)
        } catch (e: Exception) {
            // Tangani error jika terjadi
            Log.e("JobViewModel", "Error fetching jobs", e)
            emit(emptyList<Job>())  // Emit empty list jika gagal
        }
    }
    fun insert(loker: Loker) = viewModelScope.launch {
        repository.insert(loker)
        fetchAllLoker()
    }
    fun delete(loker: Loker) = viewModelScope.launch {
        repository.delete(loker)
        fetchAllLoker()
    }
    fun update(loker: Loker) = viewModelScope.launch {
        repository.update(loker)
        fetchAllLoker()
    }
}
