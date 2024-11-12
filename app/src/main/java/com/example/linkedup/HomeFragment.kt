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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedup.databinding.FragmentHomeBinding
import com.example.linkedup.item.HomeAdapter
import com.example.linkedup.item.HomeViewModel
import com.example.linkedup.item.LokerAdapter
import com.example.linkedup.utils.Loker
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var companyView: RecyclerView
    private lateinit var lokerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
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

        homeViewModel.allCompany.observe(viewLifecycleOwner) { companyList ->
            val companyAdapter = HomeAdapter(
                { context, loker -> showDeleteLokerConfirmationDialog(context, loker) },
                { id, title, deskripsi, gaji, company -> navigateToEditLokerPostFragment(id, title, deskripsi, gaji, company) },
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
        homeViewModel.allLoker.observe(viewLifecycleOwner) { lokerList ->
                val lokerAdapter = HomeAdapter(
                    { context, loker -> showDeleteLokerConfirmationDialog(context, loker) },
                    { id, title, deskripsi, gaji, company -> navigateToEditLokerPostFragment(id, title, deskripsi, gaji, company) },
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
                lokerAdapter.submitList(lokerList)
            }

        binding.addbutton.setOnClickListener {
            navigateToPostFragment()
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
                        homeViewModel.delete(loker)
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