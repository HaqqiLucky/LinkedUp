package com.example.linkedup

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.linkedup.fetch.Company
import com.example.linkedup.item.HomeViewModel
import com.example.linkedup.item.UserViewModel
import kotlinx.coroutines.launch

class CompanyListFragment : Fragment() {
    private lateinit var binding: FragmentCompanyListBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCompanyListBinding.inflate(inflater, container, false)


        lifecycleScope.launch {
            var isAdmin = false
            val res = userViewModel.getProfile()
            if (res.role == "user") {
                binding.addbutton.visibility = View.GONE
                isAdmin = false
            } else {
                binding.addbutton.visibility = View.VISIBLE
                isAdmin = true
            }

        recyclerView = binding.itemCompany
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        homeViewModel.companiesLiveData.observe(viewLifecycleOwner) { companyList ->
            companyList?.let {
                recyclerView.adapter = CompanyAdapter(it, { id, nama, alamat, web ->
                    pindahEdit(id, nama, alamat, web)
                },{ id ->
                    showDeleteLokerConfirmationDialog(id)
                }, isAdmin)
            }
        }

        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                homeViewModel.fetchAllCompany(query)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.addbutton.setOnClickListener {
            binding.textView4.text = "Tambah Company"
            binding.tambah.visibility = View.VISIBLE
            binding.cardPopUp.visibility = View.VISIBLE
        }

        binding.batal.setOnClickListener {
            binding.tambah.visibility = View.GONE
            binding.edit.visibility = View.GONE
            binding.cardPopUp.visibility = View.GONE
            binding.nama.setText("")
            binding.alamat.setText("")
            binding.web.setText("")
        }

        binding.tambah.setOnClickListener {
            insertCompany(binding.nama.text.toString(), binding.alamat.text.toString(), binding.web.text.toString())
        }

        }
        return binding.root
    }
    private fun pindahEdit(id: String, nama: String, alamat: String, web: String) {
        binding.textView4.text = "Edit Company"
        binding.cardPopUp.visibility = View.VISIBLE
        binding.edit.visibility = View.VISIBLE
        binding.nama.setText(nama)
        binding.alamat.setText(alamat)
        binding.web.setText(web)

        binding.edit.setOnClickListener {
            updateCompany(id, binding.nama.text.toString(), binding.alamat.text.toString(), binding.web.text.toString())
        }
    }
    private fun updateCompany(id: String, nama: String, alamat: String, web: String) {
        val data = Company(null, nama, alamat, web)

        lifecycleScope.launch {
            try {
                homeViewModel.updateCompany(id,data)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, CompanyListFragment())
                    .commit()
                Toast.makeText(requireContext(), "Data Company berhasil diedit", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("LokerActivity", "Error update data", e)
            }
        }
    }
    fun showDeleteLokerConfirmationDialog(id: String) {
        AlertDialog.Builder(context)
            .setTitle("Konfirmasi Hapus")
            .setMessage("Apakah Anda yakin ingin menghapus data ini?")
            .setPositiveButton("Hapus") { dialog, _ ->
                lifecycleScope.launch {
                    try {
                        homeViewModel.deleteCompany(id)
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, HomeFragment())
                            .commit()
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, CompanyListFragment())
                            .commit()
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
        val data = Company(null, name = nama, address = alamat, website = web)

        lifecycleScope.launch {
            try {
                homeViewModel.createCompany(data)
                homeViewModel.fetchAllCompany()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, CompanyListFragment())
                    .commit()
                Toast.makeText(requireContext(), "Data Company berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("LokerActivity", "Error inserting data", e)
            }
        }
    }
}