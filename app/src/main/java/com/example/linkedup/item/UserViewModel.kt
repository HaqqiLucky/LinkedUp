package com.example.linkedup.item

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.linkedup.utils.Loker
import com.example.linkedup.utils.LokerDatabase
import com.example.linkedup.utils.User
import com.example.linkedup.utils.UserDao
import com.example.linkedup.utils.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

    fun update(user: User) = viewModelScope.launch {
        repository.update(user)
        saveUserToPreferences(user)
        fetchAllUser()
    }

    fun delete(user: User) = viewModelScope.launch {
        repository.delete(user)
        fetchAllUser()
    }

    private fun saveUserToPreferences(user: User) {
        val sharedPref = getApplication<Application>().getSharedPreferences("user_prefs", AppCompatActivity.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("user_id", user._id)
            putString("user_name", user.name)
            putString("user_alamat", user.alamat)
            putString("user_email", user.email)
            putString("user_password", user.password) // Hati-hati menyimpan password, gunakan metode hashing dan salting jika perlu
            putString("user_deskripsi", user.deskripsi)
            putString("user_gender", user.jenis_kelamin)
            putBoolean("user_isAdmin", user.isAdmin)
            putString("user_image", user.image)
            apply() // Simpan perubahan
        }
    }
    fun deleteUserAccount(userId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
        repository.deleteUserById(userId)
        }
    }

    suspend fun getUserByEmailAndPassword(email: String, password: String): User? {
        return repository.getUserByEmailAndPassword(email, password)
    }

}