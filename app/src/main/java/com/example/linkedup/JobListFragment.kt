package com.example.linkedup

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedup.databinding.FragmentJobListBinding
import com.example.linkedup.fetch.Job
import com.example.linkedup.item.HomeAdapter
import com.example.linkedup.item.HomeViewModel
import com.example.linkedup.item.UserViewModel
import kotlinx.coroutines.launch
import java.io.File

class JobListFragment : Fragment() {
    private lateinit var binding: FragmentJobListBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var lokerView: RecyclerView
    private var imageFile: File? = null
    private var selectedCompanyId: String = ""
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJobListBinding.inflate(inflater, container, false)

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

        val spinner: Spinner = binding.company
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, mutableListOf())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val viewModelCompany: HomeViewModel by viewModels()

        viewModelCompany.companiesLiveData.observe(viewLifecycleOwner) { company ->
            if (company.isNullOrEmpty()) return@observe

            val companyNames = company.map { it.name }
            adapter.clear()
            adapter.addAll(companyNames)
            adapter.notifyDataSetChanged()
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parentView: AdapterView<*>, view: View?, position: Int, _id: Long) {
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


        lokerView = binding.itemloker
        lokerView.layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)

        homeViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                homeViewModel.searchJobs(query)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        homeViewModel.jobsLiveData.observe(viewLifecycleOwner) { lokerList ->
            Log.d("JobRepository", "Fetched jobs: $lokerList")
            val lokerAdapter = HomeAdapter(
                { id -> showDeleteLokerConfirmationDialog(id) },
                { id, title, deskripsi, gaji, company -> navigateToEditLokerPostFragment(id, title, deskripsi, gaji) },
                { id, title, gaji, deskripsi, waktu, company, image ->
                    val intent = Intent(activity, DetailActivity::class.java)
                    intent.putExtra("id", id)
                    intent.putExtra("title", title)
                    intent.putExtra("gaji", gaji)
                    intent.putExtra("deskripsi", deskripsi)
                    intent.putExtra("waktu", waktu)
                    intent.putExtra("company", company)
                    intent.putExtra("image", image)
                    startActivity(intent)
                },
                isAdmin
            )
            lokerView.adapter = lokerAdapter
            lokerList.let {
                lokerAdapter.submitList(it)
            }
            lokerAdapter.submitList(lokerList)
        }

        lokerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!recyclerView.canScrollVertically(1) && totalItemCount == visibleItemCount + firstVisibleItemPosition) {
//                    homeViewModel.fetchAllLokerOther()
                }
            }
        })
        binding.addbutton.setOnClickListener {
            binding.textView4.setText("Tambah Job")
            binding.cardPopUp.visibility = View.VISIBLE
            binding.image.visibility = View.VISIBLE
            binding.tambah.visibility = View.VISIBLE
            binding.edit.visibility = View.GONE
            binding.company.visibility = View.VISIBLE
            binding.title.setText("")
            binding.gaji.setText("")
            binding.deskripsi.setText("")
        }

        binding.tambah.setOnClickListener {
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

        binding.batal.setOnClickListener {
            binding.cardPopUp.visibility = View.GONE
            binding.image.visibility = View.GONE
            binding.tambah.visibility = View.GONE
            binding.edit.visibility = View.GONE
            binding.company.visibility = View.GONE
            binding.title.setText("")
            binding.gaji.setText("")
            binding.deskripsi.setText("")
        }

        binding.selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
    fun navigateToEditLokerPostFragment(id: String, title: String, deskripsi: String, gaji: Int) {
        binding.textView4.setText("Edit Job")
        binding.cardPopUp.visibility = View.VISIBLE
        binding.image.visibility = View.GONE
        binding.tambah.visibility = View.GONE
        binding.edit.visibility = View.VISIBLE
        binding.company.visibility = View.GONE

        binding.title.setText(title)
        binding.gaji.setText(gaji.toString())
        binding.deskripsi.setText(deskripsi)

        binding.edit.setOnClickListener {
            edittLoker(id, binding.title.text.toString(), binding.gaji.text.toString(), binding.deskripsi.text.toString())
        }
    }
    private fun edittLoker(id: String, title: String,gaji: String, deskripsi: String) {

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
    fun showDeleteLokerConfirmationDialog(id: String) {
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
            } catch (e: Exception) {
                Log.e("LokerActivity", "Error inserting data", e)
            } finally {
                Handler(Looper.getMainLooper()).postDelayed({
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, JobListFragment())
                    .commit()
                homeViewModel.searchJobs("")
                }, 500)
            }
        }
    }
}