package com.example.linkedup

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedup.databinding.FragmentPostBinding
import com.example.linkedup.item.LokerViewModel
import com.example.linkedup.utils.Loker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class PostFragment : Fragment() {
    private lateinit var binding: FragmentPostBinding
    private lateinit var lokerViewModel: LokerViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPostBinding.inflate(inflater, container, false)

        lokerViewModel = ViewModelProvider(this).get(LokerViewModel::class.java)

        binding.btnsubmit.setOnClickListener {
            insertLoker(binding.title.text.toString(), binding.gaji.text.toString(), binding.deskripsi.text.toString(), binding.company.selectedItem.toString())
        }

        binding.back.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_post, HomeFragment())
                .addToBackStack(null)
                .commit()
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spinner: Spinner = binding.company
        val options = listOf("Opsi 1", "Opsi 2", "Opsi 3")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun insertLoker(title: String,gaji: String, deskripsi: String, instansi: String) {
        val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())

        val data = listOf(
            Loker(title = title, gaji = gaji.toInt(), deskripsi = deskripsi, instansi = instansi, dibuat = currentDate, status = true)
        )

        lifecycleScope.launch {
            try {
                data.forEach { lokerViewModel.insert(it) }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_post, HomeFragment())
                    .addToBackStack(null)
                    .commit()
                Handler(Looper.getMainLooper()).postDelayed({
                    Toast.makeText(requireContext(), "Lowongan kerja berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                }, 100)
            } catch (e: Exception) {
                Log.e("LokerActivity", "Error inserting data", e)
            }
        }
    }

}