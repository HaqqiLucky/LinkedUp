package com.example.linkedup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.example.linkedup.databinding.FragmentGantiGambarBinding
import com.example.linkedup.fetch.RetrofitClient
import com.example.linkedup.fetch.UserApiService
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import android.util.Log

class GantiGambarFragment : Fragment(R.layout.fragment_ganti_gambar) {

    private lateinit var imageViewProfil: ImageView
    private lateinit var binding: FragmentGantiGambarBinding
    private var selectedImageUri: Uri? = null

    // Launch Gallery Intent
    private val selectImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            selectedImageUri = data?.data
            imageViewProfil.setImageURI(selectedImageUri)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentGantiGambarBinding.bind(view)
        imageViewProfil = binding.imageViewProfil

        // Tombol untuk memilih gambar
        imageViewProfil.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            selectImage.launch(intent)
        }

        // Tombol Submit untuk mengirim data ke API
        binding.btnSubmit.setOnClickListener {
            submitImage()
        }

        // Tombol Cancel untuk kembali ke fragment sebelumnya
        binding.btnCancel.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun submitImage() {
        val userImageFile = selectedImageUri?.let { getImageFile(it) }

        if (userImageFile != null && userImageFile.exists()) {
            // Kirim gambar ke server
            lifecycleScope.launch {
                try {
                    val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), userImageFile)
                    val imageRequestBody = MultipartBody.Part.createFormData("image", userImageFile.name, requestFile)
                    
                    val response = RetrofitClient.UserApiServices.submitImage(imageRequestBody)
                    
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Image Updated Successfully", Toast.LENGTH_SHORT).show()
                        parentFragmentManager.popBackStack()
                        (parentFragmentManager.findFragmentById(R.id.fragment_container) as? ProfileFragment)?.updateUI()
                    } else {
                        Toast.makeText(requireContext(), "Error uploading image", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("GantiGambarFragment", "Error uploading image", e)
                    Toast.makeText(requireContext(), "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(requireContext(), "Please select a valid image.", Toast.LENGTH_SHORT).show()
        }
    }







    // Fungsi untuk mengonversi URI gambar menjadi file
    private fun getImageFile(uri: Uri): File {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val file = File(requireContext().cacheDir, "image.jpg")
        inputStream?.copyTo(file.outputStream())
        return file
    }
}