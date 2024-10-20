package com.example.linkedup.item

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.linkedup.utils.LokerDatabase
import com.example.linkedup.utils.User
import com.example.linkedup.utils.UserRepository
import kotlinx.coroutines.launch

class SessionViewModel(application: Application) : AndroidViewModel(application) {

    private val _userSession = MutableLiveData<User?>()
    val userSession: LiveData<User?> get() = _userSession

    private val _userId = MutableLiveData<Int?>()
    val userId: LiveData<Int?> get() = _userId

    private val _userName = MutableLiveData<String?>()
    val userName: LiveData<String?> get() = _userName

    // Inisialisasi UserRepository menggunakan database
    private val userRepository: UserRepository by lazy {
        UserRepository(LokerDatabase.getDatabase(application).userDao())
    }

    // Fungsi login
    fun login(email: String, password: String) {
        Log.d("SessionViewModel", "Memulai proses login untuk email: $email")
        viewModelScope.launch {
            val user = userRepository.getUserByEmailAndPassword(email, password)
            if (user != null) {
            _userSession.value = user
            _userId.value = user?._id
            _userName.value = user?.name
                Log.d("SessionViewModel", "User ID setelah login: ${_userId.value}")
            }else {
                Log.d("SessionViewModel", "Login gagal, userId null")
            }
        }
    }

    fun logout() {
        _userSession.value = null
        _userId.value = null
    }


}