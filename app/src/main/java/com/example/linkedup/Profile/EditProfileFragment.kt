package com.example.linkedup.Profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.linkedup.databinding.FragmentEditProfileBinding
import com.example.linkedup.fetch.RetrofitClient
import com.example.linkedup.item.UserViewModel
import com.example.linkedup.fetch.UpdateProfileRequest
import kotlinx.coroutines.launch

class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        // Load current user data to show as placeholders
        loadCurrentUserData()

        binding.simpanButton.setOnClickListener {
            updateProfile()
        }

        return binding.root
    }

    private fun loadCurrentUserData() {
        lifecycleScope.launch {
            try {
                val currentUser = RetrofitClient.UserApiServices.getMe()
                
                // Set current values as hints
                binding.nama.hint = currentUser.name
                binding.deskEdit.hint = currentUser.description ?: "Masukan Deskripsi Profil"
                binding.alamatEdit.hint = currentUser.address ?: "Masukan alamat"
                
                // Set gender if available
                when (currentUser.gender) {
                    "He/Him" -> binding.He.isChecked = true
                    "She/Her" -> binding.She.isChecked = true
                }
            } catch (e: Exception) {
                Log.e("EditProfileFragment", "Error loading user data", e)
            }
        }
    }

    private fun updateProfile() {
        lifecycleScope.launch {
            try {
                val updateRequest = UpdateProfileRequest(
                    name = if (binding.nama.text.isNotEmpty()) binding.nama.text.toString() else binding.nama.hint.toString(),
                    address = if (binding.alamatEdit.text.isNotEmpty()) binding.alamatEdit.text.toString() else null,
                    description = if (binding.deskEdit.text.isNotEmpty()) binding.deskEdit.text.toString() else null,
                    gender = when {
                        binding.He.isChecked -> "He/Him"
                        binding.She.isChecked -> "She/Her"
                        else -> null
                    }
                )

                val response = userViewModel.updateProfile(updateRequest)
                if (response.isSuccessful) {
                    Toast.makeText(context, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                } else {
                    Toast.makeText(context, "Gagal mengupdate profil", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("EditProfileFragment", "Error updating profile", e)
                Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
        }
    }
}