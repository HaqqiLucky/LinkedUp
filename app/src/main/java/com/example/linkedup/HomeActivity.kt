package com.example.linkedup

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
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

class HomeActivity : AppCompatActivity() {
    private lateinit var lokerViewModel: LokerViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LokerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.itemloker)
        adapter = LokerAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        lokerViewModel.allLoker.observe(this, Observer { lokers ->
            lokers?.let { adapter.setLoker(it) }
        })

        insertDummyData()
    }
    private fun insertDummyData() {
        // Pastikan operasi ini dijalankan di luar thread utama
        lifecycleScope.launch {
            val date = Date()
            val newLoker = Loker(
                title = "Software Engineer",
                gaji = 10000000,
                deskripsi = "Mengembangkan aplikasi Android",
                instansi = "PT Contoh",
                dibuat = date,
                status = true
            )
            lokerViewModel.insert(newLoker)
        }
    }
}