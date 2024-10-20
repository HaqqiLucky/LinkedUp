package com.example.linkedup.item

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linkedup.utils.LokerDatabase
import com.example.linkedup.utils.User
import com.example.linkedup.utils.UserDao
import com.example.linkedup.utils.UserRepository
import kotlinx.coroutines.launch

class SessionViewModel(application: Application) : AndroidViewModel(application) {

    private val _userSession = MutableLiveData<User?>()
    val userSession: LiveData<User?> get() = _userSession

    private val _userId = MutableLiveData<Int?>()
    val userId: LiveData<Int?> get() = _userId

    // Inisialisasi UserRepository menggunakan database
    private val userRepository: UserRepository by lazy {
        UserRepository(LokerDatabase.getDatabase(application).userDao())
    }

    // Fungsi login
    fun login(email: String, password: String) {
        viewModelScope.launch {
            val user = userRepository.getUserByEmailAndPassword(email, password)
            _userSession.value = user
            _userId.value = user?._id
        }
    }

    fun logout() {
        _userSession.value = null
        _userId.value = null
    }
}