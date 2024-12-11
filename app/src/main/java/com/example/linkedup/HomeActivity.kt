package com.example.linkedup

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.linkedup.databinding.ActivityHomeBinding
import com.example.linkedup.fetch.AuthPrefs
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }
        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    changeFragment(HomeFragment())
                    true
                }
                R.id.job -> {
                    changeFragment(JobListFragment())
                    true
                }
                R.id.company -> {
                    changeFragment(CompanyListFragment())
                    true
                }
                R.id.logout -> {
                    logout()
                    true
                }
                R.id.profile -> {
//                    goProfile()
                    changeFragment(ProfileFragment())
                    true
                }
                else->false
            }
        }
    }
    fun changeFragment(fragment: Fragment) {
        val nowFragment = fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, nowFragment)
            .commit()
    }
    fun goProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    fun logout() {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Log Out")
            .setMessage("Apakah Anda yakin ingin Log Out?")
            .setPositiveButton("Logout") { dialog, _ ->
                lifecycleScope.launch {
                    try {
                        out()
                    } catch (e: Exception) {
                        Log.e("Home", "Error ", e)
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
    fun out() {
        val intent = Intent(this, MainActivity::class.java)
        AuthPrefs.clearToken()
        clearUserPreferences()
        startActivity(intent)
    }

    private fun clearUserPreferences() {
        val sharedPref = this.getSharedPreferences("user_prefs", AppCompatActivity.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            apply()
        }
    }
}