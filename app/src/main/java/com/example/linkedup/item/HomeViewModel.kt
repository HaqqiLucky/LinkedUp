package com.example.linkedup.item

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.linkedup.utils.Company
import com.example.linkedup.utils.CompanyRepository
import com.example.linkedup.utils.Loker
import com.example.linkedup.utils.LokerDatabase
import com.example.linkedup.utils.LokerRepository
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LokerRepository
    private val _allLoker = MutableLiveData<List<Loker>>()
    val allLoker: LiveData<List<Loker>> get() = _allLoker
    private val repository2: CompanyRepository
    private val _allCompany = MutableLiveData<List<Company>>()
    val allCompany: LiveData<List<Company>> get() = _allCompany
    private val _companyNames = MutableLiveData<List<String>>()
    val companyNames: LiveData<List<String>> get() = _companyNames

    init {
        val lokerDao = LokerDatabase.getDatabase(application).lokerDao()
        repository = LokerRepository(lokerDao)
        val companyDao = LokerDatabase.getDatabase(application).companyDao()
        repository2 = CompanyRepository(companyDao)
        fetchAllLoker()
        fetchAllCompany()
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

    private fun fetchAllCompany() {
        viewModelScope.launch {
            _allCompany.value = repository2.getAllCompany()
        }
    }

    fun insert(company: Company) = viewModelScope.launch {
        repository2.insert(company)
        fetchAllCompany()
    }

    fun delete(company: Company) = viewModelScope.launch {
        repository2.delete(company)
        fetchAllCompany()
    }

    fun update(company: Company) = viewModelScope.launch {
        repository2.update(company)
        fetchAllCompany()
    }

    fun fetchAllCompanyNames() {
        viewModelScope.launch {
            val names = repository2.getAllCompanyNames()
            _companyNames.value = names
        }
    }
}