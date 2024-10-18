package com.example.linkedup

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedup.databinding.FragmentHomeBinding
import com.example.linkedup.item.LokerAdapter
import com.example.linkedup.item.LokerViewModel
import com.example.linkedup.utils.Loker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var lokerViewModel: LokerViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lokerViewModel = ViewModelProvider(this).get(LokerViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        recyclerView = binding.itemloker  // Pastikan ID ini benar
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lokerViewModel.allLoker.observe(viewLifecycleOwner) { lokerList ->
            lokerList?.let {
                recyclerView.adapter = LokerAdapter(it, { context, loker ->
                    showDeleteLokerConfirmationDialog(context, loker)
                }, { id, title, deskripsi, gaji, company ->
                    navigateToEditLokerPostFragment(id, title, deskripsi, gaji, company)
                })
            }
        }

        binding.addpost.setOnClickListener {
            navigateToPostFragment()
        }

        binding.company.setOnClickListener {
            navigateToCompanyFragment()
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

    private fun navigateToPostFragment() {
        val postFragment = PostFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, postFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToCompanyFragment() {
        val companyFragment = CompanyListFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, companyFragment)
            .addToBackStack(null)
            .commit()
    }

    fun navigateToEditLokerPostFragment(id: Int, title: String, deskripsi: String, gaji: Int, company: String) {
        val editPostFragment = EditPostFragment()
        val bundle = Bundle()
        bundle.putInt("id", id)
        bundle.putString("title", title)
        bundle.putString("deskripsi", deskripsi)
        bundle.putInt("gaji", gaji)
        bundle.putString("company", company)
        editPostFragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, editPostFragment)
            .addToBackStack(null)
            .commit()
    }

    fun showDeleteLokerConfirmationDialog(context: Context, loker: Loker) {
        AlertDialog.Builder(context)
            .setTitle("Konfirmasi Hapus")
            .setMessage("Apakah Anda yakin ingin menghapus loker ini?")
            .setPositiveButton("Hapus") { dialog, _ ->
                lifecycleScope.launch {
                    try {
                        lokerViewModel.delete(loker)
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