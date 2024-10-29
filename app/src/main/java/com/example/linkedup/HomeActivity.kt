package com.example.linkedup

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedup.item.LokerAdapter
import com.example.linkedup.item.LokerViewModel
import com.example.linkedup.utils.Loker
import com.example.linkedup.utils.LokerDatabase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        if (savedInstanceState == null) {
            val homeFragment = HomeFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, homeFragment)
                .commit()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        lokerViewModel = ViewModelProvider(this).get(LokerViewModel::class.java)
//
//        recyclerView = findViewById(R.id.itemloker) // Pastikan ID ini benar
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        lokerViewModel.allLoker.observe(this) { lokerList ->
//            lokerList?.let {
//                recyclerView.adapter = LokerAdapter(it)
//            }
//        }

//        insertDummyData()
    }
//    private fun insertDummyData() {
//        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
//
//        val dummyData = listOf(
//            Loker(title = "Developer", gaji = 8000000, deskripsi = "Bertanggung jawab untuk pengembangan aplikasi.", instansi = "PT. A", dibuat = currentDate, status = true),
//            Loker(title = "Designer", gaji = 7000000, deskripsi = "Mendesain UI/UX aplikasi.", instansi = "PT. B", dibuat = currentDate, status = true),
//            Loker(title = "Product Manager", gaji = 9000000, deskripsi = "Mengelola produk dari awal hingga akhir.", instansi = "PT. C", dibuat = currentDate, status = true)
//        )
//
//        lifecycleScope.launch {
//            try {
//                dummyData.forEach { lokerViewModel.insert(it) }
//            } catch (e: Exception) {
//                Log.e("LokerActivity", "Error inserting dummy data", e)
//            }
//        }
//    }
}