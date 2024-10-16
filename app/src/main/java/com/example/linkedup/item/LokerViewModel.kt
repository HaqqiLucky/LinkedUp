package com.example.linkedup.item

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.linkedup.utils.Loker
import com.example.linkedup.utils.LokerDatabase
import com.example.linkedup.utils.LokerRepository
import kotlinx.coroutines.launch

// LokerViewModel.kt
class LokerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LokerRepository
    val allLoker: LiveData<List<Loker>>

    init {
        val lokerDao = LokerDatabase.getDatabase(application).lokerDao()
        repository = LokerRepository(lokerDao)
        allLoker = repository.allLoker
    }

    fun insert(loker: Loker) = viewModelScope.launch {
        repository.insert(loker)
    }

}
