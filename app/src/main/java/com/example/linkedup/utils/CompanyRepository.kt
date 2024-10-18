package com.example.linkedup.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CompanyRepository(private val companyDao: CompanyDao) {

    suspend fun insert(company: Company) {
        return withContext(Dispatchers.IO) {
            companyDao.insert(company)
        }
    }

    suspend fun delete(company: Company) {
        companyDao.delete(company)
    }

    suspend fun update(company: Company) {
        companyDao.update(company)
    }

    suspend fun getAllCompany(): List<Company> {
        return withContext(Dispatchers.IO) {
            companyDao.getAll()
        }
    }

    suspend fun getAllCompanyNames(): List<String> {
        return withContext(Dispatchers.IO) {
            companyDao.getAll().map { it.nama }
        }
    }
}