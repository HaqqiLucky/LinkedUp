package com.example.linkedup

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.linkedup.databinding.ActivityDetailBinding
import com.example.linkedup.fetch.ConfigManager

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val intent = intent
        val id = intent.getStringExtra("id")
        val title = intent.getStringExtra("title")
        val gaji = intent.getStringExtra("gaji")
        val deskripsi = intent.getStringExtra("deskripsi")
        val waktu = intent.getStringExtra("waktu")
        val company = intent.getStringExtra("company")
        val image = intent.getStringExtra("image")

        val BASE_URL = ConfigManager.getBaseUrl()
        val imageUrl = "${BASE_URL}${image}"
        Glide.with(this)
            .load(imageUrl)
            .error(R.drawable.headtest)
            .into(binding.jobImage)

        binding.jobTitle.text = title.toString()
        binding.jobSalary.text = "Salary : Rp."+gaji.toString()
        binding.jobDescription.text = deskripsi.toString()
        binding.companyName.text = "Company : "+company.toString()
        binding.postingDate.text = "Posted on : "+waktu.toString()

        binding.applyButton.setOnClickListener {
            binding.cardPopUp.visibility = View.VISIBLE
        }

        binding.batal.setOnClickListener {
            binding.cardPopUp.visibility = View.GONE
        }

    }
}