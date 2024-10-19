package com.example.linkedup.item

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.linkedup.utils.Loker
import com.example.linkedup.utils.LokerDatabase
import com.example.linkedup.utils.LokerRepository
import kotlinx.coroutines.launch

class LokerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LokerRepository
    private val _allLoker = MutableLiveData<List<Loker>>()
    val allLoker: LiveData<List<Loker>> get() = _allLoker

    init {
        val lokerDao = LokerDatabase.getDatabase(application).lokerDao()
        repository = LokerRepository(lokerDao)
        fetchAllLoker()
    }

    private fun fetchAllLoker() {
        viewModelScope.launch {
            _allLoker.value = repository.getAllLoker()
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
