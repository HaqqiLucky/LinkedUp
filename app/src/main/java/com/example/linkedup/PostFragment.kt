package com.example.linkedup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.linkedup.databinding.FragmentPostBinding
import com.example.linkedup.item.HomeViewModel
import kotlinx.coroutines.launch
import java.io.File


class PostFragment : Fragment() {
    private lateinit var binding: FragmentPostBinding
    private lateinit var homeViewModel: HomeViewModel
    private var imageFile: File? = null
    private var selectedCompanyId: String = ""
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostBinding.inflate(inflater, container, false)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        binding.btnsubmit.setOnClickListener {
            imageFile?.let { it1 ->
                insertLoker(
                    binding.title.text.toString(),
                    binding.gaji.text.toString(),
                    binding.deskripsi.text.toString(),
                    selectedCompanyId,
                    it1
                )
            }
        }

        binding.back.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinner: Spinner = binding.company
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, mutableListOf())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val viewModelCompany: HomeViewModel by viewModels()

        viewModelCompany.companiesLiveData.observe(viewLifecycleOwner) { company ->
            val companyNames = company.map { it.name }
            adapter.clear()
            adapter.addAll(companyNames)
            adapter.notifyDataSetChanged()
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parentView: AdapterView<*>, view: View?, position: Int, id: Long) {
                    selectedCompanyId = company[position]._id
                }

                override fun onNothingSelected(parentView: AdapterView<*>) {
                    selectedCompanyId = ""
                }
            }
        }
        lifecycleScope.launch {
            viewModelCompany.fetchAllCompany()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            selectedImageUri?.let {
                // Convert Uri to File
                imageFile = uriToFile(it)
                binding.selectImageButton.setImageURI(it)
            }
        }
    }
    private fun uriToFile(uri: Uri): File {
        val contentResolver = requireContext().contentResolver
        val file = File(requireContext().cacheDir, "image_${System.currentTimeMillis()}.jpg")
        contentResolver.openInputStream(uri)?.use { inputStream ->
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return file
    }
    private fun insertLoker(title: String,gaji: String, deskripsi: String, instansi: String, image: File) {
        if (image == null) {
            Toast.makeText(requireContext(), "Gambar harus dipilih", Toast.LENGTH_SHORT).show()
            return
        }
        lifecycleScope.launch {
            try {
                homeViewModel.createJob(title = title, salary = gaji.toInt(), description = deskripsi, companyId = instansi, imageFile = image)

                Toast.makeText(requireContext(), "Lowongan kerja berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                homeViewModel.searchJobs("")

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment())
                    .commit()
            } catch (e: Exception) {
                Log.e("LokerActivity", "Error inserting data", e)
            }
        }
    }

}