package com.example.linkedup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.linkedup.fetch.AuthPrefs
import com.example.linkedup.fetch.ConfigManager.isConnectedToInternet
import com.example.linkedup.fetch.RetrofitClient
import com.example.linkedup.fetch.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val token = AuthPrefs.getToken()
        // Memeriksa apakah Activity sudah di-recreate
        if (savedInstanceState == null) {
            if (!isConnectedToInternet(this)) {
                Toast.makeText(this, "No Internet Connection auto Redirect to Offline Job Save", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, OfflineActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                if (token.isNotEmpty()) {
                    verifyToken(token)
                }
                else {
                    navigateToLogin()
                }
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets


        }
    }

    private fun verifyToken(token: String) {
        // Panggil API untuk memverifikasi token dan mendapatkan data pengguna
        val apiService = RetrofitClient.UserApiServices
        val call = apiService.getCurrentUser("Bearer $token")

        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    navigateToHome()
                } else {
                    navigateToLogin()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                navigateToLogin()
            }
        })
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToLogin() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, LoginnFragment())
            .commit()
    }

}