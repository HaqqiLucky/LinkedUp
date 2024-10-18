package com.example.linkedup

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.linkedup.databinding.ActivityProfileBinding
import com.example.linkedup.utils.LokerDatabase
import com.example.linkedup.utils.User
import com.example.linkedup.utils.UserDao
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private lateinit var userDao: UserDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        val binding : ActivityProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ProfileFragment())
            .addToBackStack(null)
            .commit()







//        val db = LokerDatabase.getDatabase(this)
//        userDao = db.userDao()
//
//        lifecycleScope.launch {
//            val newUser = User(
//                name = "Haqi",
//                alamat = "London",
//                telepon = "081234567890",
//                pengalaman = "Bekerja di kantor CIA",
//                jenis_kelamin = "Laki-laki",
//                riwayat_edukasi = "S7 Teknologi Informasi",
//                alamat_saat_ini = "Kabupaten Madium",
//                isAdmin = true,
//                image = "path/to/image"
//            )
//            userDao.insert(newUser)
//        }


    }
}