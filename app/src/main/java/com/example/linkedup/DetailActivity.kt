package com.example.linkedup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.linkedup.databinding.ActivityDetailBinding
import com.example.linkedup.fetch.ConfigManager
import com.example.linkedup.fetch.RegisterForJobRequest
import com.example.linkedup.repository.JobRepository
import com.example.linkedup.utils.JobEntity
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var jobViewModel: JobViewModel
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


        jobViewModel = ViewModelProvider(this).get(JobViewModel::class.java)

        val intent = intent
        val id = intent.getStringExtra("id")
        val title = intent.getStringExtra("title")
        val gaji = intent.getStringExtra("gaji")
        val deskripsi = intent.getStringExtra("deskripsi")
        val waktu = intent.getStringExtra("waktu")
        val company = intent.getStringExtra("company")
        val image = intent.getStringExtra("image")

        val gaji2 = gaji.toString()

        binding.download.setOnClickListener {
            downloadOffline(id.toString(), title.toString(), gaji2.toInt(), deskripsi.toString(), waktu.toString(), company.toString())
        }

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

        binding.daftar.setOnClickListener {
            if (binding.deskripsiDiri.text.toString() == "" || binding.linkPortfolio.text.toString() == "") {
                Toast.makeText(this@DetailActivity, "tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show()
            } else {
                registerForJob(id.toString(), binding.deskripsiDiri.text.toString(), binding.linkPortfolio.text.toString())
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }

        }

    }

    fun registerForJob(jobId: String, deskripsi: String, portfolio: String) {
        val JobRepository = JobRepository()
        val data = RegisterForJobRequest(jobId, deskripsi, portfolio)
        lifecycleScope.launch {
            val res = JobRepository.registerForJob(data)
            Toast.makeText(this@DetailActivity, res.message, Toast.LENGTH_SHORT).show()
        }

    }

    fun downloadOffline(id: String, title: String, gaji: Int, deskripsi: String, waktu: String, company: String) {
        val data = JobEntity(id,title, gaji, deskripsi, waktu, true, "", company)
        lifecycleScope.launch {
            val res =jobViewModel.insertJob(data)
            Log.d("insert local", res.toString())
            Toast.makeText(this@DetailActivity, "Success Save Data", Toast.LENGTH_SHORT).show()
        }
    }

}