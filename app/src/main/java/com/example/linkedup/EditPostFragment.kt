package com.example.linkedup

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
import com.example.linkedup.databinding.FragmentEditPostBinding
import com.example.linkedup.item.LokerViewModel
import com.example.linkedup.utils.Loker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditPostFragment : Fragment() {
    private lateinit var binding: FragmentEditPostBinding
    private lateinit var lokerViewModel: LokerViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditPostBinding.inflate(inflater, container, false)
        lokerViewModel = ViewModelProvider(this).get(LokerViewModel::class.java)

        val id: String = arguments?.getInt("id").toString()
        binding.title.setText(arguments?.getString("title"))
        binding.deskripsi.setText(arguments?.getString("deskripsi"))
        binding.gaji.setText(arguments?.getInt("gaji").toString())
        binding.company.text = arguments?.getString("company")

        binding.btnsubmit.setOnClickListener {
            edittLoker(id.toInt(), binding.title.text.toString(), binding.gaji.text.toString(), binding.deskripsi.text.toString(), binding.company.text.toString() )
        }

        return binding.root
    }

    private fun edittLoker(id: Int, title: String,gaji: String, deskripsi: String, instansi: String) {
        val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())

        val data = Loker(_id = id, title = title, gaji = gaji.toInt(), deskripsi = deskripsi, instansi = instansi, dibuat = currentDate, status = true)

        lifecycleScope.launch {
            try {
                lokerViewModel.update(data)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment())
                    .addToBackStack(null)
                    .commit()
                Handler(Looper.getMainLooper()).postDelayed({
                    Toast.makeText(requireContext(), "Lowongan kerja berhasil diedit", Toast.LENGTH_SHORT).show()
                }, 100)
            } catch (e: Exception) {
                Log.e("LokerActivity", "Error Updating data", e)
            }
        }
    }
}