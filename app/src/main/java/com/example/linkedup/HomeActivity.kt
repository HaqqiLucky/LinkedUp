package com.example.linkedup

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.linkedup.databinding.ActivityHomeBinding
import com.example.linkedup.fetch.AuthPrefs
import com.example.linkedup.item.UserViewModel
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel
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
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        lifecycleScope.launch {
            val res = userViewModel.getProfile()
            if (res.role == "user") {
                binding.sideNav.menu.findItem(R.id.jobapply).isVisible = false
                binding.sideNav.menu.findItem(R.id.jobhistory).isVisible = true
            } else {
                binding.sideNav.menu.findItem(R.id.jobapply).isVisible = true
                binding.sideNav.menu.findItem(R.id.jobhistory).isVisible = false
            }

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
                R.id.more -> {
                    if (binding.sideNavu.visibility == View.VISIBLE) {
                        binding.sideNavu.visibility = View.GONE
                    } else {
                        binding.sideNavu.visibility = View.VISIBLE
                    }
                    true
                }
                R.id.profile -> {
                    changeFragment(ProfileFragment())
                    true
                }
                else->false
            }
        }

        binding.sideNav.setNavigationItemSelectedListener {
            when (it.itemId){
                R.id.jobsave -> {
                    goDraft()
                    true
                }
                R.id.logout -> {
                    logout()
                    true
                }
                R.id.jobapply -> {
                    true
                }
                R.id.jobhistory -> {
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
    fun goDraft() {
        val intent = Intent(this, OfflineActivity::class.java)
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