package com.example.linkedup.item

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.linkedup.utils.Loker
import com.example.linkedup.utils.LokerDatabase
import com.example.linkedup.utils.User
import com.example.linkedup.utils.UserDao
import com.example.linkedup.utils.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(application: Application):AndroidViewModel(application) {
    private val repository : UserRepository
    private val _allUser = MutableLiveData<List<User>>()
    val allUser: LiveData<List<User>>get() = allUser

    init {
        val UserDao = LokerDatabase.getDatabase(application).userDao()
        repository = UserRepository(UserDao)
        fetchAllUser()
    }

    private fun fetchAllUser(){
        viewModelScope.launch {
            _allUser.value = repository.getAllUser()
        }
    }
    fun insert(user: User) = viewModelScope.launch {
        repository.insert(user)
        fetchAllUser()
    }

    suspend fun getUserByEmailAndPassword(email: String, password: String): User? {
        return repository.getUserByEmailAndPassword(email, password)
    }

}