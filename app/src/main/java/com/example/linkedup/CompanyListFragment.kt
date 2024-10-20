package com.example.linkedup

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedup.databinding.FragmentCompanyListBinding
import com.example.linkedup.item.CompanyAdapter
import com.example.linkedup.item.CompanyViewModel
import com.example.linkedup.utils.Company
import com.example.linkedup.utils.Loker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CompanyListFragment : Fragment() {
    private lateinit var binding: FragmentCompanyListBinding
    private lateinit var companyViewModel: CompanyViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        companyViewModel = ViewModelProvider(this).get(CompanyViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCompanyListBinding.inflate(inflater, container, false)

        recyclerView = binding.itemCompany
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        companyViewModel.allCompany.observe(viewLifecycleOwner) { companyList ->
            companyList?.let {
                recyclerView.adapter = CompanyAdapter(it, { id, nama, alamat, web ->
                    pindahEdit(id, nama, alamat, web)
                },{ context, company ->
                    showDeleteLokerConfirmationDialog(context, company)
                })
            }
        }
        binding.addpost.setOnClickListener {
            navigateToPostFragment()
        }

        binding.home.setOnClickListener {
            navigateToHomeFragment()
        }

        binding.addbutton.setOnClickListener {
            binding.cardPopUp.visibility = View.VISIBLE
        }

        binding.batal.setOnClickListener {
            binding.cardPopUp.visibility = View.GONE
        }

        binding.tambah.setOnClickListener {
            insertCompany(binding.nama.text.toString(), binding.alamat.text.toString(), binding.web.text.toString())
        }

        binding.profile.setOnClickListener {
            val intent = Intent(activity, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.logout.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun navigateToHomeFragment() {
        val homeFragment = HomeFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, homeFragment)
            .addToBackStack(null)
            .commit()
    }
    private fun navigateToPostFragment() {
        val postFragment = PostFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, postFragment)
            .addToBackStack(null)
            .commit()
    }
    private fun pindahEdit(id: Int, nama: String, alamat: String, web: String) {
        val homeFragment = FormEditCompanyFragment()
        val bundle = Bundle()
        bundle.putInt("id", id)
        bundle.putString("nama", nama)
        bundle.putString("alamat", alamat)
        bundle.putString("web", web)
        homeFragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, homeFragment)
            .addToBackStack(null)
            .commit()
    }
    fun showDeleteLokerConfirmationDialog(context: Context, company: Company) {
        AlertDialog.Builder(context)
            .setTitle("Konfirmasi Hapus")
            .setMessage("Apakah Anda yakin ingin menghapus data ini?")
            .setPositiveButton("Hapus") { dialog, _ ->
                lifecycleScope.launch {
                    try {
                        companyViewModel.delete(company)
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

    private fun insertCompany(nama: String, alamat: String, web: String) {
        val data = Company(nama = nama, alamat = alamat, web = web)

        lifecycleScope.launch {
            try {
                companyViewModel.insert(data)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, CompanyListFragment())
                    .commit()
                Handler(Looper.getMainLooper()).postDelayed({
                    Toast.makeText(requireContext(), "Data Company berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                }, 100)
            } catch (e: Exception) {
                Log.e("LokerActivity", "Error inserting data", e)
            }
        }
    }
}