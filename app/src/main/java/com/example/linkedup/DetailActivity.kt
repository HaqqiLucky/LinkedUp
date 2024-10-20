package com.example.linkedup

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val titleView: TextView = findViewById(R.id.job_title)
        val gajiView: TextView = findViewById(R.id.job_salary)
        val deskripsiView: TextView = findViewById(R.id.job_description)
        val companyView: TextView = findViewById(R.id.company_name)
        val waktuView: TextView = findViewById(R.id.posting_date)

        val intent = intent
        val title = intent.getStringExtra("title")
        val gaji = intent.getStringExtra("gaji")
        val deskripsi = intent.getStringExtra("deskripsi")
        val waktu = intent.getStringExtra("waktu")
        val company = intent.getStringExtra("company")

        titleView.text = title.toString()
        gajiView.text = "Salary : Rp."+gaji.toString()
        deskripsiView.text = deskripsi.toString()
        companyView.text = "Company : "+company.toString()
        waktuView.text = "Posted on : "+waktu.toString()


    }
}