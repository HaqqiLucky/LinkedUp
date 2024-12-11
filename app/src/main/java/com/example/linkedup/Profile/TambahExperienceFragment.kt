package com.example.linkedup.Profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.linkedup.databinding.FragmentTambahExperienceBinding
import com.example.linkedup.item.ExperienceViewModel
import com.example.linkedup.item.SessionViewModel
import kotlinx.coroutines.launch
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.linkedup.fetch.RetrofitClient

class TambahExperienceFragment : Fragment() {
    private var _binding: FragmentTambahExperienceBinding? = null
    private val binding get() = _binding!!
    private lateinit var experienceViewModel: ExperienceViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTambahExperienceBinding.inflate(inflater, container, false)
        experienceViewModel = ViewModelProvider(requireActivity()).get(ExperienceViewModel::class.java)
        
        // Get current user data
        lifecycleScope.launch {
            try {
                val currentUser = RetrofitClient.UserApiServices.getMe()
                currentUser._id?.toIntOrNull()?.let { userId ->
                    experienceViewModel.setUserId(userId)
                    Log.d("TambahExperience", "userId from API: $userId")
                } ?: run {
                    Log.e("TambahExperience", "User ID is null from API")
                }
            } catch (e: Exception) {
                Log.e("TambahExperience", "Error getting current user", e)
            }
        }
        
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buatButton.setOnClickListener {
            val judul = binding.Judul.text.toString()
            val tempat = binding.namaTempat.text.toString()
            val isHighlighted = binding.highlightedEdit.checkedRadioButtonId == binding.Ya.id

            if (judul.isEmpty() || tempat.isEmpty()) {
                Toast.makeText(context, "Mohon isi semua field", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val response = experienceViewModel.addExperience(judul, tempat, isHighlighted)
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Experience berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                        parentFragmentManager.popBackStack()
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                        Toast.makeText(context, "Gagal: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("TambahExperience", "Error adding experience", e)
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}