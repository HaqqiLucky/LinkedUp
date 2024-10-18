package com.example.linkedup.item

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.linkedup.utils.Company
import com.example.linkedup.utils.LokerDatabase
import com.example.linkedup.utils.CompanyRepository
import kotlinx.coroutines.launch

class CompanyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CompanyRepository
    private val _allCompany = MutableLiveData<List<Company>>()
    val allCompany: LiveData<List<Company>> get() = _allCompany

    private val _companyNames = MutableLiveData<List<String>>()
    val companyNames: LiveData<List<String>> get() = _companyNames

    init {
        val companyDao = LokerDatabase.getDatabase(application).companyDao()
        repository = CompanyRepository(companyDao)
        fetchAllCompany()
    }

    private fun fetchAllCompany() {
        viewModelScope.launch {
            _allCompany.value = repository.getAllCompany()
        }
    }

    fun insert(company: Company) = viewModelScope.launch {
        repository.insert(company)
        fetchAllCompany()
    }

    fun delete(company: Company) = viewModelScope.launch {
        repository.delete(company)
        fetchAllCompany()
    }

    fun update(company: Company) = viewModelScope.launch {
        repository.update(company)
        fetchAllCompany()
    }

    fun fetchAllCompanyNames() {
        viewModelScope.launch {
            val names = repository.getAllCompanyNames()
            _companyNames.value = names
        }
    }

}