package com.example.linkedup

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedup.databinding.FragmentHomeBinding
import com.example.linkedup.item.HomeAdapter
import com.example.linkedup.item.HomeViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var companyView: RecyclerView
    private lateinit var lokerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        lifecycleScope.launch {
            homeViewModel.searchJobs("")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        companyView = binding.itemcompany
        lokerView = binding.itemloker
        lokerView.layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        companyView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        homeViewModel.companiesLiveData.observe(viewLifecycleOwner) { companyList ->
            val companyAdapter = HomeAdapter(
                { id -> showDeleteLokerConfirmationDialog(id) },
                { id, title, deskripsi, gaji, company -> navigateToEditLokerPostFragment(id, title, deskripsi, gaji) },
                { title, gaji, deskripsi, waktu, company ->
                    val intent = Intent(activity, DetailActivity::class.java)
                    intent.putExtra("title", title)
                    intent.putExtra("gaji", gaji)
                    intent.putExtra("deskripsi", deskripsi)
                    intent.putExtra("waktu", waktu)
                    intent.putExtra("company", company)
                    startActivity(intent)
                }
            )
            companyView.adapter = companyAdapter
            companyAdapter.submitList(companyList)
        }
        homeViewModel.jobsLiveData.observe(viewLifecycleOwner) { lokerList ->
            Log.d("JobRepository", "Fetched jobs: $lokerList")
                val lokerAdapter = HomeAdapter(
                    { id -> showDeleteLokerConfirmationDialog(id) },
                    { id, title, deskripsi, gaji, company -> navigateToEditLokerPostFragment(id, title, deskripsi, gaji) },
                    { title, gaji, deskripsi, waktu, company ->
                        val intent = Intent(activity, DetailActivity::class.java)
                        intent.putExtra("title", title)
                        intent.putExtra("gaji", gaji)
                        intent.putExtra("deskripsi", deskripsi)
                        intent.putExtra("waktu", waktu)
                        intent.putExtra("company", company)
                        startActivity(intent)
                    }
                )
                lokerView.adapter = lokerAdapter
                lokerList.let {
                    lokerAdapter.submitList(it)
                }
                lokerAdapter.submitList(lokerList)
            }

        binding.batal.setOnClickListener {
            binding.cardPopUp.visibility = View.GONE
            binding.title.setText("")
            binding.gaji.setText("")
            binding.deskripsi.setText("")
        }


        return binding.root
    }

    fun navigateToEditLokerPostFragment(id: Int, title: String, deskripsi: String, gaji: Int) {
        binding.textView4.setText("Edit Job")
        binding.cardPopUp.visibility = View.VISIBLE

        binding.title.setText(title)
        binding.gaji.setText(gaji.toString())
        binding.deskripsi.setText(deskripsi)

        binding.edit.setOnClickListener {
            edittLoker(id, binding.title.text.toString(), binding.gaji.text.toString(), binding.deskripsi.text.toString())
        }
    }

    private fun edittLoker(id: Int, title: String,gaji: String, deskripsi: String) {

        lifecycleScope.launch {
            try {
                homeViewModel.updateJob(id,title,gaji.toInt(),deskripsi)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, JobListFragment())
                    .commit()
                Toast.makeText(requireContext(), "Lowongan kerja berhasil diedit", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("LokerActivity", "Error Updating data", e)
            }
        }
    }

    fun showDeleteLokerConfirmationDialog(id: Int) {
        AlertDialog.Builder(context)
            .setTitle("Konfirmasi Hapus")
            .setMessage("Apakah Anda yakin ingin menghapus loker ini?")
            .setPositiveButton("Hapus") { dialog, _ ->
                lifecycleScope.launch {
                    try {
                        homeViewModel.deleteJob(id)
                        homeViewModel.searchJobs("")
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