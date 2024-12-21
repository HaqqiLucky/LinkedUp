package com.example.linkedup

import android.app.AlertDialog
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.linkedup.databinding.ActivityOfflineBinding
import com.example.linkedup.fetch.JobResponse
import com.example.linkedup.item.HomeViewModel
import com.example.linkedup.item.JobAdapter
import com.example.linkedup.utils.JobDao
import com.example.linkedup.utils.JobEntity
import com.example.linkedup.utils.LokerDatabase
import kotlinx.coroutines.launch

class OfflineActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOfflineBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var jobViewModel: JobViewModel

    private lateinit var jobAdapter: JobAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOfflineBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = binding.itemView
        recyclerView.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)

        // Inisialisasi ViewModels
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        jobViewModel = ViewModelProvider(this).get(JobViewModel::class.java)

        // Setup adapter untuk RecyclerView
        jobAdapter = JobAdapter({ id, title, gaji, deskripsi, waktu, company, image ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("title", title)
            intent.putExtra("gaji", gaji)
            intent.putExtra("deskripsi", deskripsi)
            intent.putExtra("waktu", waktu)
            intent.putExtra("company", company)
            intent.putExtra("image", image)
            startActivity(intent)
        }, { id -> showDeleteLokerConfirmationDialog(id) })
        recyclerView.adapter = jobAdapter

        // Observer untuk mengambil data dari API

        jobViewModel.jobssLiveData.observe(this, Observer { jobs ->
            jobAdapter.submitList(jobs)
        })

    }
    fun showDeleteLokerConfirmationDialog(id: JobEntity) {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Hapus")
            .setMessage("Apakah Anda yakin ingin menghapus loker ini dari daftar offline?")
            .setPositiveButton("Hapus") { dialog, _ ->
                lifecycleScope.launch {
                    try {
                        jobViewModel.deleteJobs(id)
                    } catch (e: Exception) {
                        Log.e("HomeFragment", "Error hapus data", e)
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
}

class JobViewModel(application: Application) : AndroidViewModel(application) {

    private val jobDao: JobDao
    private val _jobssLiveData = MutableLiveData<List<JobEntity>>()
    val jobssLiveData: LiveData<List<JobEntity>> get() = _jobssLiveData

        init {
        // Menginisialisasi database dan DAO
            val db = Room.databaseBuilder(application, LokerDatabase::class.java, "loker_database").build()
            jobDao = db.jobDao() // Menggunakan LiveData agar bisa diamati
            getAllJobs()
        }

        // Menyediakan LiveData untuk mengamati perubahan data pekerjaan
        fun getAllJobs() {
            viewModelScope.launch {
                try {
                    val res = jobDao.getAllJobs()
                    _jobssLiveData.postValue(res)

                } catch (e: Exception) {
                    _jobssLiveData.postValue(emptyList())

                }
            }
        }

        // Fungsi untuk menyisipkan data pekerjaan
        fun insertJob(job: JobEntity) {
            viewModelScope.launch {
                jobDao.insertJob(job)
            }
        }

        fun deleteJobs(job: JobEntity) {
            viewModelScope.launch {
                jobDao.delete(job)
            }
        }
    }
